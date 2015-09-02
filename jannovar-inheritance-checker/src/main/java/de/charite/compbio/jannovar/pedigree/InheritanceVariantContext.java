package de.charite.compbio.jannovar.pedigree;

import java.util.ArrayList;
import java.util.List;

import de.charite.compbio.jannovar.pedigree.compatibilitychecker.InheritanceCompatibilityChecker;
import htsjdk.variant.variantcontext.VariantContext;

/**
 * 
 * This class is an extension of {@link VariantContext} for the {@link InheritanceCompatibilityChecker}.
 * 
 * @author <a href="mailto:max.schubach@charite.de">Max Schubach</a>
 *
 */
public class InheritanceVariantContext extends VariantContext {

	/**
	 * Builder to transform a {@link List} of {@link VariantContext} into a {@link List} of
	 * {@link InheritanceVariantContext}.
	 * 
	 * @author <a href="mailto:max.schubach@charite.de">Max Schubach</a>
	 *
	 */
	static final class ListBuilder {

		/**
		 * The final list.
		 */
		private List<InheritanceVariantContext> list = new ArrayList<InheritanceVariantContext>();

		/**
		 * @param vcList
		 *            A {@link List} of {@link VariantContext} that should be transformed.
		 * @return The {@link ListBuilder} to continue with {@link #build()}.
		 */
		public ListBuilder variants(List<VariantContext> vcList) {
			for (VariantContext vc : vcList) {
				list.add(new InheritanceVariantContext(vc));
			}

			return this;
		}

		/**
		 * Build the list.
		 * 
		 * @return the transformed list.
		 */
		public List<InheritanceVariantContext> build() {
			return list;
		}

	}

	/**
	 * Default ID for serialization
	 */
	private static final long serialVersionUID = 6381923752159274326L;

	/**
	 * <code>true</code> if the {@link VariantContext} matches the {@link ModeOfInheritance}. This flag is set by the
	 * {@link InheritanceCompatibilityChecker}.
	 */
	private boolean matchInheritance;

	/**
	 * Default constructor. Copies the given {@link VariantContext} and sets the {@link #matchInheritance} to
	 * <code>false</code>.
	 * 
	 * @param other
	 *            Other {@link VariantContext} to copy.
	 */
	protected InheritanceVariantContext(VariantContext other) {
		super(other);
		this.matchInheritance = false;
	}

	/**
	 * @param matchInheritance
	 *            <code>true</code> if the {@link InheritanceVariantContext} matches the {@link ModeOfInheritance}
	 */
	public void setMatchInheritance(boolean matchInheritance) {
		this.matchInheritance = matchInheritance;
	}

	/**
	 * Single {@link Person} {@link Genotype} getter. Used for the Single mode of the
	 * {@link InheritanceCompatibilityChecker}.
	 * 
	 * @return the first {@link htsjdk.variant.variantcontext.Genotype} stored in the {@link VariantContext}
	 */
	public Genotype getSingleSampleGenotype() {
		return getGenotype(getGenotype(0));
	}

	/**
	 * getter of {@link #matchInheritance}.
	 * 
	 * @return <code>true</code> if the {@link InheritanceVariantContext} matches the {@link ModeOfInheritance}.
	 */
	public boolean isMatchInheritance() {
		return matchInheritance;
	}

	/**
	 * Transforms a HTSJDK {@link htsjdk.variant.variantcontext.Genotype} into an Jannovar {@link Genotype}.
	 * 
	 * Note that this code is not optimal. It makes false decissions if we have multiple alternative alleles. For
	 * example A*,G,T: genotype is G/T => {@value Genotype#HETEROZYGOUS} (always correct), genotype is G/G =>
	 * {@value Genotype#HOMOZYGOUS_ALT} (correct or false). But the combination of both (child + mother) we maybe have
	 * to consider G/G as {@value Genotype#HOMOZYGOUS_REF}...
	 * 
	 * @param g
	 *            HTSJDK {@link htsjdk.variant.variantcontext.Genotype} to transform
	 * @return The transformed HTSJDK genotype into a Jannovar {@link Genotype}.
	 */
	private Genotype getGenotype(htsjdk.variant.variantcontext.Genotype g) {
		// TODO(mschubach) What about multi allelic spots...
		if (g.isHet() || g.isHetNonRef())
			return Genotype.HETEROZYGOUS;
		else if (g.isHomRef())
			return Genotype.HOMOZYGOUS_REF;
		else if (g.isHomVar())
			return Genotype.HOMOZYGOUS_ALT;
		else
			return Genotype.NOT_OBSERVED;
	}

	/**
	 * Gets the Jannovar {@link Genotype} for a {@link Person} (name must be the same than in the VCF file)
	 * 
	 * @param p A {@link Person} in the {@link InheritanceVariantContext}
	 * @return The {@link Genotype} of the {@link Person}.
	 */
	public Genotype getGenotype(Person p) {
		return getGenotype(getGenotype(p.getName()));
	}

}
