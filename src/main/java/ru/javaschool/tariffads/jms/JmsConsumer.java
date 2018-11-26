package ru.javaschool.tariffads.jms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.javaschool.tariffads.dto.TariffPlanDto;
import ru.javaschool.tariffads.service.TariffService;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

@MessageDriven(activationConfig = {
            @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/TariffsMQ"),
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
            @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class JmsConsumer implements MessageListener {

    @Inject
    private TariffService tariffService;

    private Logger logger = LogManager.getLogger(JmsConsumer.class);

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println(textMessage.getText() + "from queue");
            List<TariffPlanDto> tariffPlanDtos = tariffService.getAllTariffs();
            System.out.println(tariffPlanDtos);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

