package com.gonzobeans;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       System.out.println( "Hello World!" );

        var resolver = new Resolver();
        resolver.addItem("/notifications/v1/", "a.*", (input) -> "Hello");
        resolver.addItem("/notifications/v1/", "b.*", (input) -> "Facetime, " + input);
        resolver.addItem("/crt/v3/", "a.*",  (input) -> "hello, " + input);
        resolver.addItem("/crt/v3/", "b.*", (input) -> "byebye, " + input);
        resolver.addItem("/crt/v3/", "c.*/gree", (input) -> "two for the show, " + input);
        resolver.addItem("/crt/v3/", "c.*", (input) -> "one for the money, " + input);

        System.out.println(resolver.resolve("/crt/v3/ceeelocrt/v3//green").apply("seeelogreen"));

    }
}
