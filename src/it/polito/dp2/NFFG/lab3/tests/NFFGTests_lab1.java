package it.polito.dp2.NFFG.lab3.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NamedEntityReader;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierFactory;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

import org.junit.Assert;


public class NFFGTests_lab1 {
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    private static NffgVerifier referenceNffgVerifier;
    private static NffgVerifier testNffgVerifier;
    private static long testcase;

    public NFFGTests_lab1() {
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.setProperty("it.polito.dp2.NFFG.NffgVerifierFactory", "it.polito.dp2.NFFG.Random.NffgVerifierFactoryImpl");
        referenceNffgVerifier = NffgVerifierFactory.newInstance().newNffgVerifier();
        System.setProperty("it.polito.dp2.NFFG.NffgVerifierFactory", "it.polito.dp2.NFFG.sol3.client2.NffgVerifierFactory");
        testNffgVerifier = NffgVerifierFactory.newInstance().newNffgVerifier();
        Long var0 = Long.getLong("it.polito.dp2.NFFG.Random.testcase");
        if (var0 == null) {
            testcase = 0L;
        } else {
            testcase = var0.longValue();
        }

    }

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull("Internal tester error during test setup: null reference", referenceNffgVerifier);
        Assert.assertNotNull("Could not run tests: the implementation under test generated a null NffgVerifier", testNffgVerifier);
    }

    private void compareString(String var1, String var2, String var3) {
        Assert.assertNotNull("NULL " + var3, var2);
        Assert.assertEquals("Wrong " + var3, var1, var2);
    }

    private void compareTime(Calendar var1, Calendar var2, String var3) {
        if (testcase == 2L) {
            Assert.assertNotNull(var1);
            Assert.assertNotNull("Null " + var3, var2);
            Calendar var4 = (Calendar) var1.clone();
            var4.add(12, 1);
            Calendar var5 = (Calendar) var1.clone();
            var5.add(12, -1);
            boolean var6 = var2.after(var5) && var2.before(var4);
            Assert.assertTrue("Wrong " + var3, var6);
        }
    }

    @Test
    public final void testGetNffgs() {
        Set var1 = referenceNffgVerifier.getNffgs();
        Set var2 = testNffgVerifier.getNffgs();
        this.compareNffgSets(var1, var2);
    }

    private void compareNffgSets(Set<NffgReader> var1, Set<NffgReader> var2) {
        if ((var1 != null || var2 == null) && (var1 == null || var2 != null)) {
            if (var1 == null && var2 == null) {
                Assert.assertTrue("There are no Nffgs!", true);
            } else {
                Assert.assertEquals("Wrong Number of Nffgs", (long) var1.size(), (long) var2.size());
                TreeSet var3 = new TreeSet(new NFFGTests_lab1.NamedEntityReaderComparator());
                TreeSet var4 = new TreeSet(new NFFGTests_lab1.NamedEntityReaderComparator());
                var3.addAll(var1);
                var4.addAll(var2);
                Iterator var5 = var3.iterator();
                Iterator var6 = var4.iterator();

                while (var5.hasNext() && var6.hasNext()) {
                    this.compareNffgReader((NffgReader) var5.next(), (NffgReader) var6.next());
                }

            }
        } else {
            Assert.fail("getNffgs returns null when it should return non-null or vice versa");
        }
    }

    private void compareNffgReader(NffgReader var1, NffgReader var2) {
        Assert.assertNotNull("Internal tester error: null nffg reader", var1);
        Assert.assertNotNull("Unexpected null nffg reader", var2);
        this.compareString(var1.getName(), var2.getName(), "nffg name");
        this.compareTime(var1.getUpdateTime(), var2.getUpdateTime(), "update time");
        this.compareNodeSets(var1.getNodes(), var2.getNodes());
    }

    private void compareNodeSets(Set<NodeReader> var1, Set<NodeReader> var2) {
        if ((var1 != null || var2 == null) && (var1 == null || var2 != null)) {
            if (var1 == null && var2 == null) {
                Assert.assertTrue("There are no nodes!", true);
            } else {
                Assert.assertEquals("Wrong Number of nodes", (long) var1.size(), (long) var2.size());
                TreeSet var3 = new TreeSet(new NFFGTests_lab1.NamedEntityReaderComparator());
                TreeSet var4 = new TreeSet(new NFFGTests_lab1.NamedEntityReaderComparator());
                var3.addAll(var1);
                var4.addAll(var2);
                Iterator var5 = var3.iterator();
                Iterator var6 = var4.iterator();

                while (var5.hasNext() && var6.hasNext()) {
                    this.compareNodeReader((NodeReader) var5.next(), (NodeReader) var6.next());
                }

            }
        } else {
            Assert.fail("getNodes returns null when it should return non-null or vice versa");
        }
    }

    private void compareNodeReader(NodeReader var1, NodeReader var2) {
        Assert.assertNotNull("Internal tester error: null node reader", var1);
        Assert.assertNotNull("A null NodeReader has been found", var2);
        this.compareString(var1.getName(), var2.getName(), "node name");
        Assert.assertEquals("wrong functional type", var1.getFuncType(), var2.getFuncType());
        this.compareLinkReaderSets(var1.getLinks(), var2.getLinks(), "links");
    }

    private void compareLinkReaderSets(Set<LinkReader> var1, Set<LinkReader> var2, String var3) {
        if ((var1 != null || var2 == null) && (var1 == null || var2 != null)) {
            if (var1 == null && var2 == null) {
                Assert.assertTrue("There are no links!", true);
            } else {
                Assert.assertEquals("Wrong Number of links", (long) var1.size(), (long) var2.size());
                TreeSet var4 = new TreeSet(new NFFGTests_lab1.NamedEntityReaderComparator());
                TreeSet var5 = new TreeSet(new NFFGTests_lab1.NamedEntityReaderComparator());
                var4.addAll(var1);
                var5.addAll(var2);
                Iterator var6 = var4.iterator();
                Iterator var7 = var5.iterator();

                while (var6.hasNext() && var7.hasNext()) {
                    this.compareLinkReader((LinkReader) var6.next(), (LinkReader) var7.next());
                }

            }
        } else {
            Assert.fail("getLinks returns null when it should return non-null or vice versa");
        }
    }

    private void compareLinkReader(LinkReader var1, LinkReader var2) {
        this.compareString(var1.getName(), var2.getName(), "node name");
        this.compareString(var1.getSourceNode().getName(), var2.getSourceNode().getName(), "source node");
        this.compareString(var1.getDestinationNode().getName(), var2.getDestinationNode().getName(), "destination node");
    }

    @Test
    public final void testGetPolicies() {
        Set var1 = referenceNffgVerifier.getPolicies();
        Set var2 = testNffgVerifier.getPolicies();
        this.comparePolicySets(var1, var2);
    }

    private void comparePolicySets(Set<PolicyReader> var1, Set<PolicyReader> var2) {
        if ((var1 != null || var2 == null) && (var1 == null || var2 != null)) {
            if (var1 == null && var2 == null) {
                Assert.assertTrue("There are no policies!", true);
            } else {
                Assert.assertEquals("Wrong Number of policies", (long) var1.size(), (long) var2.size());
                TreeSet var3 = new TreeSet(new NFFGTests_lab1.NamedEntityReaderComparator());
                TreeSet var4 = new TreeSet(new NFFGTests_lab1.NamedEntityReaderComparator());
                var3.addAll(var1);
                var4.addAll(var2);
                Iterator var5 = var3.iterator();
                Iterator var6 = var4.iterator();

                while (var5.hasNext() && var6.hasNext()) {
                    this.comparePolicyReader((PolicyReader) var5.next(), (PolicyReader) var6.next());
                }

            }
        } else {
            Assert.fail("getPolicies returns null when it should return non-null or vice versa");
        }
    }

    private void comparePolicyReader(PolicyReader rpr, PolicyReader tpr) {
        assertNotNull("Internal tester error: null policy reader", rpr);
        assertNotNull("A null PolicyReader has been found", tpr);
        compareString(rpr.getName(), tpr.getName(), "policy name");
        assertTrue("Wrong ispositive, (" + rpr.getName() + " - " + tpr.getName() + ")", rpr.isPositive() == tpr.isPositive());

        System.out.println(" > comparing my policy: " + rpr.getName() + " and test policy: " + tpr.getName());
        compareVerificationResultReader(rpr.getResult(), tpr.getResult());
        System.out.println(" > OK");
    }

    private void compareVerificationResultReader(VerificationResultReader rr, VerificationResultReader tr) {
        if ((rr == null) && (tr != null) || (rr != null) && (tr == null)) {
            if (tr == null) {
                System.out.println("  > Verification result must be NULL");

                if (rr != null) {
                    System.out.println("   > MY Verification result in NOT NULL");
                }
            }

            if (tr != null) {
                System.out.println("  > Verification result must be NOT NULL");

                if (rr == null) {
                    System.out.println("  > MY Verification result in NULL");
                }
            }
            fail("FAIL: verification result is null when it should be non-null or vice versa");
            return;
        }

        if ((rr == null) && (tr == null)) {
            // System.out.println("No verification result to compare.");;
            return;
        }
        assertNotNull("Internal tester error: null policy referenced by verification result", rr.getPolicy());
        assertNotNull("Verification result references null policy", tr.getPolicy());
        compareString(rr.getPolicy().getName(), tr.getPolicy().getName(), "policy name");
        assertTrue("Wrong result", rr.getVerificationResult().equals(tr.getVerificationResult()));
        compareString(rr.getVerificationResultMsg(), tr.getVerificationResultMsg(), "verification result message");
        compareTime(rr.getVerificationTime(), tr.getVerificationTime(), "verification time");
    }

    class NamedEntityReaderComparator implements Comparator<NamedEntityReader> {
        NamedEntityReaderComparator() {
        }

        public int compare(NamedEntityReader var1, NamedEntityReader var2) {
            return var1.getName().compareTo(var2.getName());
        }
    }
}
