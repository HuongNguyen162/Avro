import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {
        Customer customer1 = new Customer(1, "the_first","the_first@gmail.com","0912345678", "16", new Address("Cau Giay", "Ha Noi"), sex.MALE);

        Customer customer2 = new Customer();
        customer2.setId(2);
        customer2.setCustomername("the_second");
        customer2.setEmailAddress("the_second@gmail.com");
        customer2.setPhoneNumber("0923456789");
        customer2.setBirthdate("22");
        customer2.setAddress(new Address("Hai Ba Trung", "Ha Noi"));
        customer2.setSex(sex.FEMALE);

        Customer customer3 = Customer.newBuilder().setId(3).setCustomername("the_third").setEmailAddress("the_third@gmail.com").setPhoneNumber("0945678910").setBirthdate("18").setAddress(new Address("Ha Dong", "Ha Noi")).setSex(sex.FEMALE).build();

        // Serialize customer1, customer2, customer3 to disk
        Customer[] customers = {customer1, customer2, customer3};
        serializing(customers);

        // Deserialize
        deserializing(new File("customers.avro"));
    }

    public static void serializing(Customer[] customers) throws IOException {
        // Serialize customer1, customer2 and customer3 to disk
        DatumWriter<Customer> customerDatumWriter = new SpecificDatumWriter<Customer>(Customer.class);
        DataFileWriter<Customer> dataFileWriter = new DataFileWriter<Customer>(customerDatumWriter);
        dataFileWriter.create(customers[0].getSchema(), new File("customers.avro"));
        for(Customer u : customers) {
            dataFileWriter.append(u);
        }
        dataFileWriter.close();
    }

    public static void deserializing(File file ) throws IOException {
        // Deserialize customers from disk
        DatumReader<Customer> customerDatumReader = new SpecificDatumReader<Customer>(Customer.class);
        ;
        DataFileReader<Customer> dataFileReader = new DataFileReader<Customer>(file, customerDatumReader);
        Customer customer = null;
        while (dataFileReader.hasNext()) {
            // Reuse customer object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            customer = dataFileReader.next(customer);
            System.out.println(customer);
        }
    }
}