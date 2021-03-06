package de.charite.compbio.jannovar.pedigree;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import de.charite.compbio.jannovar.pedigree.compatibilitychecker.CompatibilityCheckerException;

public class CompatibilityCheckerXRecessiveStupidSmallFemaleTest extends CompatibilityCheckerTestBase {

	@Before
	public void setUp() throws PedParseException {
		// STUPID PEDIGREE! The male must be affected. Otherwise it can only be
		// a de-novo second second hit. But we do not want to cover special
		// cases. So this case should always return no variant!
		ImmutableList.Builder<PedPerson> individuals = new ImmutableList.Builder<PedPerson>();
		individuals.add(new PedPerson("ped", "I.1", "0", "0", Sex.MALE, Disease.UNAFFECTED)); // father
		individuals.add(new PedPerson("ped", "I.2", "0", "0", Sex.FEMALE, Disease.UNAFFECTED)); // mother
		individuals.add(new PedPerson("ped", "II.1", "I.1", "I.2", Sex.MALE, Disease.UNAFFECTED)); // son
		individuals.add(new PedPerson("ped", "II.2", "I.1", "I.2", Sex.FEMALE, Disease.AFFECTED)); // daughter
		PedFileContents pedFileContents = new PedFileContents(new ImmutableList.Builder<String>().build(),
				individuals.build());
		this.pedigree = new Pedigree(pedFileContents, "ped");

		this.names = ImmutableList.of("I.1", "I.2", "II.1", "II.2");
	}

	@Test
	public void testSizeOfPedigree() {
		Assert.assertEquals(4, pedigree.getMembers().size());
	}

	@Test
	public void testCaseNegativesOneVariant() throws CompatibilityCheckerException {
		Assert.assertFalse(buildCheckerXR(lst(HET, HET, HET, HET)).run());
		Assert.assertFalse(buildCheckerXR(lst(REF, REF, REF, REF)).run());
		Assert.assertFalse(buildCheckerXR(lst(ALT, ALT, ALT, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(UKN, UKN, UKN, UKN)).run());

		Assert.assertFalse(buildCheckerXR(lst(HET, REF, HET, HET)).run());
		Assert.assertFalse(buildCheckerXR(lst(HET, HET, REF, HET)).run());
		Assert.assertFalse(buildCheckerXR(lst(HET, REF, REF, REF)).run());
		Assert.assertFalse(buildCheckerXR(lst(REF, REF, REF, HET)).run());
		Assert.assertFalse(buildCheckerXR(lst(REF, REF, HET, REF)).run());

		Assert.assertFalse(buildCheckerXR(lst(HET, ALT, HET, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(HET, HET, ALT, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(ALT, REF, HET, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(ALT, REF, REF, ALT)).run());

		Assert.assertFalse(buildCheckerXR(lst(ALT, HET, REF, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(ALT, ALT, REF, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(ALT, REF, HET, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(ALT, REF, ALT, ALT)).run());

		Assert.assertFalse(buildCheckerXR(lst(HET, ALT, ALT, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(HET, UKN, ALT, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(HET, ALT, UKN, ALT)).run());

		Assert.assertFalse(buildCheckerXR(lst(ALT, REF, REF, HET)).run());
		Assert.assertFalse(buildCheckerXR(lst(ALT, REF, REF, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(REF, REF, REF, HET)).run());

		Assert.assertFalse(buildCheckerXR(lst(HET, HET, ALT, ALT)).run());
		// Assert.assertFalse(buildCheckerXR(lst(REF, REF, REF, ALT)).run());

		// at least one hom_alt
		Assert.assertFalse(buildCheckerXR(lst(HET, UKN, UKN, UKN)).run());

		// A male cannot be unaffected
		Assert.assertFalse(buildCheckerXR(lst(REF, HET, HET, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(HET, REF, REF, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(HET, REF, HET, ALT)).run());

		Assert.assertFalse(buildCheckerXR(lst(REF, HET, REF, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(REF, UKN, REF, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(UKN, HET, REF, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(REF, HET, UKN, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(UKN, HET, UKN, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(REF, UKN, UKN, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(UKN, UKN, REF, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(UKN, UKN, UKN, ALT)).run());

	}

	@Test
	public void testCaseNegativesTwoVariants() throws CompatibilityCheckerException {
		// compound heterozygous
		Assert.assertFalse(buildCheckerXR(lst(HET, ALT, HET, HET), lst(HET, UKN, HET, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(HET, HET, HET, ALT), lst(HET, UKN, HET, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(HET, REF, ALT, REF), lst(HET, UKN, HET, ALT)).run());
		Assert.assertFalse(buildCheckerXR(lst(ALT, REF, HET, REF), lst(HET, UKN, HET, ALT)).run());

		Assert.assertFalse(buildCheckerXR(lst(REF, ALT, HET, REF), lst(HET, REF, HET, REF)).run());
		Assert.assertFalse(buildCheckerXR(lst(ALT, REF, HET, REF), lst(REF, HET, HET, REF)).run());
		Assert.assertFalse(buildCheckerXR(lst(HET, HET, HET, REF), lst(REF, HET, HET, REF)).run());
		Assert.assertFalse(buildCheckerXR(lst(REF, HET, HET, REF), lst(HET, HET, HET, REF)).run());

		Assert.assertFalse(buildCheckerXR(lst(REF, HET, HET, HET), lst(HET, REF, HET, HET)).run());
	}

	@Test
	public void testCasePositiveOneVariant() throws CompatibilityCheckerException {

	}

	@Test
	public void testCasePositiveTwoVariants() throws CompatibilityCheckerException {

	}

}
