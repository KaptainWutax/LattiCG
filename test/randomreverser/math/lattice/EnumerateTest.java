package randomreverser.math.lattice;

import org.junit.Test;
import randomreverser.math.component.BigMatrix;
import randomreverser.math.component.BigVector;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EnumerateTest {
    @Test
    public void testEnumerate1() {
        BigMatrix basis = BigMatrix.fromString(
            "{\n" +
            "    { -44389541525945,    -2563735374277,    1919643326751,   34120343161907,  -34524058483785,    2857120431851,  -44296256836593,  -25331898860445,  -22332526090201,    2548979409563,   -3146024681985,    4516321902483,   13735989355415,    8763154157899,    -232356675857,       3704800707,   -2887070554617 },\n" +
            "    {  25414690081607,    33529990846267,  -33911545502177,    8148045747507,    2099922802871,   26708598976491,   19476546341135,    9550626723171,  -20153828340441,  -17897080937573,  -24835491796225,   -8980834047853,   17360624212119,  -11545135947189,    -580924425233,     609342242499,   -5778465854713 },\n" +
            "    {  36302748918381,    -2017043114391,   -9778401484619,   21645590383377,  -10345062548419,    6853987857401,  -37539297650939,  -22545933939935,   37141821989645,   24421989849737,    8708253803093,   -6887944242127,  -30988356081955,   -2940955387367,     495279641253,   -1687162606015,   -4359875881043 },\n" +
            "    { -28139147364290,   -22312104335770,    4148925644142,   28032038075350,   24285720903198,    2709950342854,  -17676512205234,   57882328965942,    3441812406782,   -8857806533338,  -17600189678290,   -3441099054954,   11223101793246,  -33666002028154,     159395226126,    -434675051530,     920593400766 },\n" +
            "    { -18845060522773,     3408183292431,   11782818990691,   34159859654183,   -7704965439333,   26041474393087,  -26289646539373,   43253294381463,   -2932139544757,  -15878765771537,    8402913809347,   -1338645292025,  -24432741385989,    2381594985695,    -392580567821,    2900071691639,   -1743929102933 },\n" +
            "    {   6674714629448,   -15155666337880,  -13603230971256,   36279758269416,   -3449166126648,   -1237912677848,  -35443956962552,   24352287392360,  -24726161263032,   -4354706928472,  -20699385965688,   17621424357608,  -24270511729976,    4920542015272,    1275111441416,   -2375292590232,   -1487309736120 },\n" +
            "    {  14408164060809,   -39762644769195,    -582794055631,    4762877283037,  -11989378929127,    8755923310245,    5622919176769,   39854957951917,   17232603780777,  -18859371037707,   -1886311291567,   16142584605053,    7175771535417,    5802235995717,     437842954593,   -5834511915955,    1591285179081 },\n" +
            "    { -24693740277652,    29911045924348,  -28034626235316,   25675105414236,    6910858824492,   -9098507368004,   16833048825100,   -7053830875620,  -11366655127060,   -8887988868740,   18928589967820,   30433938529244,    4158731502764,    -982673767108,     297395380876,  -11023880503908,   -2596984134804 },\n" +
            "    {  35003335260660,    12431039587556,  -13842293699308,    7608380475780,    6388388619572,    1873763487524,  -23359537575340,   22494309604804,   16122640794740,  -30538976125596,   -6185980961900,   -2628081224188,  -19463058597964,   -2047338934364,   -1190208274220,   -2420909157820,    4263521525492 },\n" +
            "    {   3134268671932,   -22451453644020,   23076620010524,   10165398792172,    2704809117564,   32944393639884,  -24623401686564,  -19276142806356,  -10970982038724,   20110909343884,   26931776383900,  -12397446137492,   34182285409020,    2287826109772,     149764213084,     519829922860,   -7937345327428 },\n" +
            "    {  -7916911246630,   -20443899538734,    6258080935786,  -40696725165022,    6923012036218,  -31764327535118,    7367258291210,   16472773659714,    9168236905498,  -19143917692142,   24759376261802,  -23660331020702,   -8664416064582,   34414635219506,    -275321195702,   -1339479616894,   -6080785568422 },\n" +
            "    {   8338043711476,    24669832061668,    3096023741204,    1339667691396,   -3829667939532,   -3689604420316,  -30743088803756,   -8220267500604,     641367063156,   38510021548900,  -39669682601580,   -2933432212476,   12618158927284,    8797007080868,     117612414676,    2300364480580,   -4392113793804 },\n" +
            "    { -10845234448258,    -5723629272666,   32394808334254,   16001461086486,   -9669754387874,  -15598821771770,   -2862001281906,   27618277926006,  -19980676855234,   -7047430500250,   34823666737006,    2095453779414,    3288845529118,     302594879686,     184489404494,   -7405748552394,    -994436033538 },\n" +
            "    { -21297325867282,    -2038058295466,    2573342989214,  -12292377643450,   -4241392975922,  -10414816411978,  -36252544452738,    4225715524774,   13671817326254,   14028121486358,   11681852453214,    -225400538874,    8676061883278,  -41463631437962,    -267939780290,    9870339310438,    2852387460718 },\n" +
            "    { -24653990591439,     7182135316189,   -2306235850215,  -30689982383451,  -14458453723583,   17104059023277,   19787353623209,   -1040264503307,    6504309979473,   20301086059901,    6414904880185,    1839312751173,   -1269147722399,  -24231014160307,     107795470025,    2810893928853,    9948341957233 },\n" +
            "    {   7815283902776,     9476207459544,    3732268677112,   42079475983512,  -10649651583816,  -29614288489896,   -8805547442312,    7912800556568,   -7603088558024,    8331294179288,   -2816769360136,   -2202980130920,   -7239409613896,    3519753856344,    -416769345928,    -477384261352,    3084051629880 },\n" +
            "    { -16691284157604,    17253202726444,   -9574078574404,  -13489416402932,   -1600493669092,   -6966316874516,  -20532432904068,   13281292287180,   18995859516124,   -3796888442964,    5382698527804,  -16996956832372,   20053693283484,   21292184729196,    -300368366596,    3444956187212,    3779626625628 }\n" +
            "}\n"
        ).transpose();

        BigVector lower = BigVector.fromString("211106232532992,  211106232532981,  210828868589894,  199388147328707,  161385748837116,  108479823158593,  185398950615714,  185126754296559,   73966775769528,   62839209804621,   83194595169726,  145472338376155,  -22881604128716,  -51152864657895,  119381387175578,  107356151384967,  249574878786160");
        BigVector upper = BigVector.fromString("281474976710656,  281474976710645,  281197612767558,  269756891506371,  231754493014780,  178848567336257,  255767694793378,  255495498474223,  144335519947192,  133207953982285,  153563339347390,  215841082553819,   47487140048948,   19215879519769,  121580410431130,  124948337429383,  267167064830576");

        List<BigVector> correct = new ArrayList<>();
        correct.add(BigVector.fromString("-24, 1, 25, -3, -13, 27, -17, -32, -56, -40, -64, -35, 51, -8, -39, -47, 69"));
        correct.add(BigVector.fromString("-24, 1, 25, -3, -13, 27, -17, -31, -56, -39, -63, -35, 50, -8, -39, -46, 69"));
        correct.add(BigVector.fromString("-23, 1, 26, -3, -13, 26, -17, -32, -56, -41, -65, -35, 52, -9, -40, -48, 70"));
        correct.add(BigVector.fromString("-23, 2, 26, -3, -14, 27, -17, -32, -57, -41, -64, -36, 52, -8, -40, -48, 70"));
        correct.add(BigVector.fromString("-24, 1, 26, -3, -13, 27, -17, -32, -57, -41, -64, -35, 52, -8, -40, -48, 70"));
        correct.add(BigVector.fromString("-23, 2, 26, -3, -13, 26, -16, -32, -57, -41, -64, -35, 52, -8, -40, -48, 70"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -13, 27, -18, -32, -57, -41, -64, -36, 52, -8, -40, -48, 70"));
        correct.add(BigVector.fromString("-23, 2, 26, -3, -13, 27, -17, -32, -57, -41, -64, -36, 51, -8, -40, -48, 70"));
        correct.add(BigVector.fromString("-23, 2, 26, -3, -14, 27, -17, -32, -56, -40, -64, -36, 52, -8, -39, -47, 70"));
        correct.add(BigVector.fromString("-23, 2, 26, -3, -13, 27, -17, -32, -57, -41, -64, -36, 51, -8, -40, -47, 70"));
        correct.add(BigVector.fromString("-24, 2, 26, -4, -13, 26, -16, -32, -58, -41, -65, -36, 52, -7, -41, -47, 70"));
        correct.add(BigVector.fromString("-24, 1, 26, -3, -13, 26, -17, -32, -57, -41, -65, -36, 51, -8, -41, -47, 70"));
        correct.add(BigVector.fromString("-24, 1, 25, -3, -13, 27, -17, -31, -56, -40, -64, -35, 51, -8, -40, -47, 70"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -13, 27, -17, -32, -57, -41, -64, -35, 52, -8, -40, -47, 70"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -13, 27, -17, -32, -57, -41, -64, -35, 52, -8, -39, -47, 70"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -13, 27, -17, -32, -57, -40, -64, -36, 51, -8, -40, -47, 70"));
        correct.add(BigVector.fromString("-24, 1, 25, -3, -13, 27, -17, -31, -56, -39, -64, -35, 50, -9, -39, -46, 70"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -13, 27, -17, -31, -57, -40, -63, -35, 51, -8, -39, -46, 70"));
        correct.add(BigVector.fromString("-24, 1, 25, -4, -13, 27, -16, -31, -56, -39, -64, -35, 51, -8, -39, -46, 70"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -13, 27, -17, -32, -58, -41, -64, -36, 51, -8, -40, -46, 70"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -14, 27, -17, -32, -57, -40, -64, -36, 52, -8, -39, -46, 70"));
        correct.add(BigVector.fromString("-24, 1, 26, -3, -13, 26, -17, -32, -57, -42, -66, -35, 53, -8, -42, -50, 71"));
        correct.add(BigVector.fromString("-24, 2, 27, -3, -13, 26, -17, -32, -58, -42, -65, -36, 53, -8, -41, -48, 71"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -14, 27, -17, -32, -57, -41, -65, -36, 53, -8, -40, -48, 71"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -14, 27, -17, -32, -57, -41, -65, -36, 52, -8, -40, -48, 71"));
        correct.add(BigVector.fromString("-24, 1, 26, -3, -13, 27, -18, -32, -57, -41, -65, -36, 52, -9, -40, -48, 71"));
        correct.add(BigVector.fromString("-23, 2, 27, -2, -14, 26, -17, -32, -57, -41, -65, -36, 52, -9, -40, -48, 71"));
        correct.add(BigVector.fromString("-24, 2, 26, -4, -13, 27, -17, -33, -58, -42, -65, -36, 53, -8, -41, -48, 71"));
        correct.add(BigVector.fromString("-24, 1, 26, -3, -13, 26, -17, -32, -57, -41, -65, -35, 52, -9, -40, -48, 71"));
        correct.add(BigVector.fromString("-23, 2, 27, -2, -14, 26, -17, -33, -58, -42, -65, -36, 53, -9, -41, -48, 71"));
        correct.add(BigVector.fromString("-24, 1, 26, -3, -13, 26, -17, -32, -57, -41, -65, -35, 52, -9, -40, -47, 71"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -13, 27, -17, -32, -58, -41, -64, -35, 52, -8, -40, -47, 71"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -14, 27, -17, -32, -57, -40, -64, -37, 51, -8, -40, -46, 71"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -14, 27, -17, -32, -57, -40, -64, -36, 51, -8, -40, -46, 71"));
        correct.add(BigVector.fromString("-24, 2, 26, -3, -13, 27, -17, -32, -57, -40, -64, -36, 51, -9, -39, -46, 71"));

        long nanos = System.nanoTime();

        List<BigVector> results = Enumerate.enumerate(basis, lower, upper, 24);

        nanos = System.nanoTime() - nanos;

        System.out.printf("%.3f%n", nanos / 1.0e9);

        assertEquals(correct.size(), results.size());
        assertTrue(results.containsAll(correct));
    }
}
