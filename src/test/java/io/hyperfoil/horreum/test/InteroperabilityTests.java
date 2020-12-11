package io.hyperfoil.horreum.test;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class InteroperabilityTests {

    private static final boolean DEBUG_OUTPUT = false;

    @Test
    public void testContextArrayMapping() {
        Context context = Context.newBuilder()
                .allowAllAccess(true)
                .build();
        Value record = context.asValue(DataPoints.largeSet);
        assertEquals(400,record.getArraySize());
    }

    @Test
    public void runFunction() {
        try (Context context = Context.newBuilder()
                .allowAllAccess(true)
                .build()) {
            Value function = context.eval("R", "function(x) x + 1");
            assert function.canExecute();
            int result = function.execute(41).asInt();
            assertEquals(42, result);
        }
    }


    @Test
    public void runRrandomscript() {
        ScriptUtils.loadScript("eDivisiveMeanRandom.R"
                , function -> {
                    assert function.canExecute();
                    Value result = function.execute();
                    if (DEBUG_OUTPUT) {
                        ScriptUtils.printResultArray(result);
                    }
                    assertNotEquals(0, result.getArraySize());
                }
                , msg -> fail(msg));

    }

    @Test
    public void runFunctionFromResource() {
        ScriptUtils.loadScript("rFunction.R", function -> {
                    int result = function.execute(41).asInt();
                    assertEquals(42, result);
                }
                , msg -> fail(msg));
    }

    @Test
    public void testMatrix() {
        ScriptUtils.loadScript("convertMatrix.R", function -> {
                    Value result = function.execute();
                    if (DEBUG_OUTPUT) {
                        ScriptUtils.printResultArray(result);
                    }
                    assertEquals(2, result.getArraySize());
                }
                , msg -> fail(msg));
    }

    @Test
    public void testMatrixParams() {
        ScriptUtils.loadScript("convertMatrixParams.R", function -> {
                    Value result = function.execute(DataPoints.smallSetAsStringVector());
                    if (DEBUG_OUTPUT) {
                        ScriptUtils.printResultArray(result);
                    }
                    assertEquals(40, result.getArraySize());
                }
                , msg -> fail(msg));
    }


    @Test
    public void runRscript() {
        ScriptUtils.loadScript("eDivisiveMean.R"
                , function -> {
                    Value result = function.execute(DataPoints.smallSet);
                    if (DEBUG_OUTPUT)
                        ScriptUtils.printResultArray(result);
                    fail("Should fail to execute");
                }
                , msg -> {}); //test should fail to execute
    }

    @Test
    public void passMatrixAsFile(){

        File dataFile = ScriptUtils.writeArrayToFile(DataPoints.largeSet);

        System.out.printf("Data file: %s\n", dataFile.getAbsolutePath());

        ScriptUtils.loadScript("eDivisiveMeanFile.R", function -> {
                    Value result = function.execute(dataFile.getAbsolutePath(), 1);
                    if (DEBUG_OUTPUT) {
                        ScriptUtils.printResultArray(result);
                    }
                    assertEquals(2, result.getArraySize());
                }
                , msg -> fail(msg));


    }

    @Test
    public void passMultiDimMatrixAsFile(){

        File dataFile = ScriptUtils.writeArrayToFile(DataPoints.largeSet);

        System.out.printf("Data file: %s\n", dataFile.getAbsolutePath());

        ScriptUtils.loadScript("eDivisiveMeanFile.R", function -> {
                    Value result = function.execute(dataFile.getAbsolutePath(), 1);
                    if (DEBUG_OUTPUT) {
                        ScriptUtils.printResultArray(result);
                    }
                    assertEquals(3, result.getArraySize());
                }
                , msg -> fail(msg));
    }

    @Test
    public void testDynamicArray(){

        Double[] dataArray = DataPoints.generateChangeSet(400,2);
        File dataFile = ScriptUtils.writeArrayToFile(dataArray);
        System.out.printf("Data file: %s\n", dataFile.getAbsolutePath());

        ScriptUtils.loadScript("eDivisiveMeanFile.R", function -> {
                    Value result = function.execute(dataFile.getAbsolutePath(), 1);
                    if (DEBUG_OUTPUT) {
                        ScriptUtils.printResultArray(result);
                    }
                    assertEquals(2, result.getArraySize());
                }
                , msg -> fail(msg));
    }


    @Test
    public void testMultiDimensionalArray(){

        Double[] dataArray1 = DataPoints.generateChangeSet(400,2);
        Double[] dataArray2 = DataPoints.generateChangeSet(400,3);

        File dataFile = ScriptUtils.writeArrayToFile(dataArray1, dataArray2);

        System.out.printf("Data file: %s\n", dataFile.getAbsolutePath());

        ScriptUtils.loadScript("eDivisiveMeanFile.R", function -> {
                    Value result = function.execute(dataFile.getAbsolutePath(), 2);
                    if (DEBUG_OUTPUT) {
                        ScriptUtils.printResultArray(result);
                    }
                    assertEquals(3, result.getArraySize());
                }
                , msg -> fail(msg));


    }


    @Test
    public void testEaggloArray(){

        Double[] dataArray = DataPoints.generateChangeSet(400,4);
        File dataFile = ScriptUtils.writeArrayToFile(dataArray);
        System.out.printf("Data file: %s\n", dataFile.getAbsolutePath());

        ScriptUtils.loadScript("eAggloFile.R", function -> {
                    Value result = function.execute(dataFile.getAbsolutePath(), 1, 40, 10);
                    if (DEBUG_OUTPUT) {
                        ScriptUtils.printResultArray(result);
                    }
                    assertEquals(4, result.getArraySize());

                }
                , msg -> fail(msg));
    }

    @Test
    public void testEaggloArraySmallChange(){

        Double[] dataArray = DataPoints.generateChangeSet(400,4, 1f,0.5f );
        File dataFile = ScriptUtils.writeArrayToFile(dataArray);
        System.out.printf("Data file: %s\n", dataFile.getAbsolutePath());

        ScriptUtils.loadScript("eAggloFile.R", function -> {
                    Value result = function.execute(dataFile.getAbsolutePath(), 1, 40, 10);
                    if (DEBUG_OUTPUT) {
                        ScriptUtils.printResultArray(result);
                    }
                    assertEquals(4, result.getArraySize());

                }
                , msg -> fail(msg));
    }

    @Test
    public void testEaggloArraySmallChangeSmallDataSet(){

        Double[] dataArray = DataPoints.generateChangeSet(200,2, 1f,0.5f );
        File dataFile = ScriptUtils.writeArrayToFile(dataArray);
        System.out.printf("Data file: %s\n", dataFile.getAbsolutePath());

        ScriptUtils.loadScript("eAggloFile.R", function -> {
                    Value result = function.execute(dataFile.getAbsolutePath(), 1, 20, 10);
                    if (DEBUG_OUTPUT) {
                        ScriptUtils.printResultArray(result);
                    }
                    assertEquals(2, result.getArraySize());
                }
                , msg -> fail(msg));
    }

}
