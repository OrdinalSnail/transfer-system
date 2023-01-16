package com.nordea.transfer_system.message;

import com.nordea.transfer_system.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class MessageValidator {

    private static final Logger LOG = LoggerFactory.getLogger(MessageValidator.class);

    private Validator initValidator(String xsdPath) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        InputStream stream = AppUtil.getResourceAsStream(xsdPath);
        Source schemaFile = new StreamSource(stream);
        Schema schema = factory.newSchema(schemaFile);
        return schema.newValidator();
    }

    public boolean isValid(String xsdPath, String xml) {
        try {
            Validator validator = initValidator(xsdPath);
            validator.validate(new StreamSource(new StringReader(xml)));
            return true;
        } catch (SAXException | IOException e) {
            LOG.error("Validation error", e);
            return false;
        }
    }


}
