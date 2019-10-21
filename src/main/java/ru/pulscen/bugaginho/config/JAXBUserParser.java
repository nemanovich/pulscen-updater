package ru.pulscen.bugaginho.config;

import ru.pulscen.bugaginho.Users;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;


public class JAXBUserParser {
    public static Users readUsers(String xmlFileName) {
        Users users = new Users();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(users.getClass().getPackage().getName());
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new StreamSource(Users.class.getClassLoader().getResourceAsStream("Users.xsd")));
            unmarshaller.setSchema(schema);
            JAXBElement<Users> JAXBusers;

            try (FileInputStream fis = new FileInputStream(xmlFileName)) {
                JAXBusers = (JAXBElement<Users>) unmarshaller.unmarshal(fis);
                users = JAXBusers.getValue();
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse users from file", e);
        }
        return users;
    }
}
