package com.nordea.transfer_system.message;

import com.nordea.transfer_system.generated.xsd.OutcomeType;
import com.nordea.transfer_system.generated.xsd.TransferRequestType;
import com.nordea.transfer_system.generated.xsd.TransferResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class MessageConverter {

    private static final Logger LOG = LoggerFactory.getLogger(MessageConverter.class);

    /**
     * Marshalling Response to MXL
     * Business logic method, that means not null input parameters
     *
     * @param obj non null response object
     * @return response xml for queue
     */
    public String makeXMlResponse(TransferResponseType obj) {
        try {
            JAXBContext context = JAXBContext.newInstance(TransferResponseType.class);
            Marshaller mar = context.createMarshaller();
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            mar.marshal(obj, sw);
            return sw.toString();
        } catch (JAXBException e) {
            LOG.error("Marshalling error", e);
            return null;
        }
    }

    /**
     * Unmarshalling xml to Object
     * Business logic method, that means not null input parameters
     *
     * @param xml non null input xml
     * @return input request
     */
    public TransferRequestType makeRequest(String xml) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TransferRequestType.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            return (TransferRequestType) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            LOG.error("Unmarshalling error", e);
            return null;
        }
    }


    /**
     * Convert  request to process after processing step.
     * Business logic method, that means not null input parameters
     *
     * @param request not null incoming request
     * @param type    outcome type
     * @return response based on request data
     */
    public TransferResponseType convertToResponse(TransferRequestType request, OutcomeType type) {
        TransferResponseType response = new TransferResponseType();
        response.setRequestId(request.getRequestId());
        response.setTargetAccountNumber(request.getTargetAccountNumber());
        response.setAction(request.getAction());
        response.setCurrency(request.getCurrency());
        response.setQuantity(request.getQuantity());
        response.setOutcome(type);
        return response;
    }


}
