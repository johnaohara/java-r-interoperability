package io.hyperfoil.horreum.test;

import org.graalvm.polyglot.*;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.function.Consumer;

public class ScriptUtils {

   public static void printResultArray(Value result){

        int arrSize = Long.valueOf(result.getArraySize()).intValue();

        Double[] changePoints = new Double[arrSize];
        for(int counter = 0 ; counter < arrSize; counter++){
            changePoints[counter] = result.getArrayElement(counter).asDouble();
        }

        Arrays.stream(changePoints).forEach(value -> System.out.printf("%s\n", value));

    }

    public interface FailureCallback{
       void failure(String reason);
    }

    public static void loadScript(String scriptResource, Consumer<Value> resultCallback, FailureCallback failureCallback){

        try {
            URL resource = ScriptUtils.class.getClassLoader().getResource(scriptResource);
            Source source = Source.newBuilder("R", resource)
                    .interactive(false)
                    .build();

            try (Context context = Context.newBuilder()
                    .allowAllAccess(true)
                    .build()) {
                try {
                    Value function = context.eval(source);
                    assert function.canExecute();
                    resultCallback.accept(function);
                } catch (PolyglotException pe){
                    failureCallback.failure(pe.getMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
