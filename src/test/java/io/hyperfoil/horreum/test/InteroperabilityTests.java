package io.hyperfoil.horreum.test;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.Test;

import static org.junit.Assert.*;

public class InteroperabilityTests {

    @Test
    public void testContextArrayMapping() {
        Context context = Context.newBuilder()
                .allowAllAccess(true)
                .build();
        Value record = context.asValue(DataPoints.largeSet);
        assertEquals(400,record.getArraySize());
    }

    @Test
    public void printHelloWorld() {
        System.out.println("Hello Java!");
        try (Context context = Context.newBuilder()
                .allowAllAccess(true)
                .build()) {
            context.eval("R", "print('Hello R!');");
        }
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
                    assertNotEquals(0, result.getArraySize());
                    ScriptUtils.printResultArray(result);
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
                    ScriptUtils.printResultArray(result);
                }
                , msg -> fail(msg));
    }

    @Test
    public void testMatrixParams() {
        ScriptUtils.loadScript("convertMatrixParams.R", function -> {
                    Value result = function.execute(DataPoints.smallSetAsStringVector());
                    ScriptUtils.printResultArray(result);
                }
                , msg -> fail(msg));
    }


    @Test
    public void runRscript() {
        ScriptUtils.loadScript("eDivisiveMean.R"
                , function -> {
                    Value result = function.execute(DataPoints.smallSet);
                    ScriptUtils.printResultArray(result);
                    fail("Should fail to execute");
                }
                , msg -> {}); //test should fail to execute
    }

}
