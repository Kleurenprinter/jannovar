package jannovar.annotation.builders;

import jannovar.annotation.Annotation;
import jannovar.common.VariantType;
import jannovar.exception.InvalidGenomeChange;
import jannovar.reference.GenomeChange;
import jannovar.reference.TranscriptInfo;

/**
 * Dispatches annotation building to the specific classes, depending on their {@link GenomeChange#getType}.
 *
 * @author Manuel Holtgrewe <manuel.holtgrewe@charite.de>
 */
public class AnnotationBuilderDispatcher {
	/** transcript to build annotation for */
	public final TranscriptInfo transcript;
	/** genomic change to build annotation for */
	public final GenomeChange change;

	public AnnotationBuilderDispatcher(TranscriptInfo transcript, GenomeChange change) {
		this.transcript = transcript;
		this.change = change;
	}

	/**
	 * @return {@link Annotation} for {@link #transcript} and {@link #change}
	 *
	 * @throws InvalidGenomeChange
	 *             if there is a problem with {@link #change}
	 */
	public Annotation build() throws InvalidGenomeChange {
		if (transcript == null)
			return new Annotation(null, "INTERGENIC", VariantType.INTERGENIC);

		switch (change.getType()) {
		case SNV:
			return SingleNucleotideSubstitutionBuilder.buildAnnotation(transcript, change);
		case DELETION:
			return DeletionAnnotationBuilder.buildAnnotation(transcript, change);
		case INSERTION:
			return InsertionAnnotationBuilder.buildAnnotation(transcript, change);
		case BLOCK_SUBSTITUTION:
		default:
			return BlockSubstitutionAnnotationBuilder.buildAnnotation(transcript, change);
		}
	}

}
