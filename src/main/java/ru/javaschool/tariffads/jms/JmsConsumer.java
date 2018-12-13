package ru.javaschool.tariffads.jms;

import org.apache.log4j.Logger;
import ru.javaschool.tariffads.service.TariffService;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(activationConfig = {
            @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/TariffsMQ"),
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
            @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class JmsConsumer implements MessageListener {

    @Inject
    private TariffService tariffService;

    private Logger logger = Logger.getLogger(JmsConsumer.class);

    public void onMessage(Message message) {
        logger.debug("message received");
        TextMessage textMessage = (TextMessage) message;
        try {
            if("updated".equals(textMessage.getText())){
                logger.debug("call tariffService.updateTariffs() from jms consumer");
                tariffService.updateTariffs();
            }
        } catch (JMSException e) {
            logger.debug(e.getStackTrace());
        }
    }
}

