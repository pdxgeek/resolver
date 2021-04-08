package com.gonzobeans;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // add items to the resolver in the format of:
        // {prefix} , {regex match}, {lambda expression}
        // resolver will group common prefixes together, and check regex for matching in order
        // so be careful how you order and define your regex matching
        // resolver will also cache the results of specific urls
        
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
