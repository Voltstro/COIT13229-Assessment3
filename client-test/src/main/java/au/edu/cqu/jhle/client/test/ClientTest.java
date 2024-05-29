package au.edu.cqu.jhle.client.test;

import au.edu.cqu.jhle.shared.models.Product;

/**
 *
 * @author Administrator
 */
public class ClientTest {

    public static void main(String[] args) {
        System.out.println("Hello World!");


        Product product = new Product(0, "Test", "Test", 123.1, "Test");
        System.out.println(product.toString());
    }
}
