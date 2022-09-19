package labshoppubsub.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import labshoppubsub.DeliveryApplication;
import labshoppubsub.domain.DeliveryStarted;
import lombok.Data;

@Entity
@Table(name = "Delivery_table")
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String customerId;

    private Long orderId;

    @PostPersist
    public void onPostPersist() {
        DeliveryStarted deliveryStarted = new DeliveryStarted(this);
        deliveryStarted.publishAfterCommit();
    }

    public static DeliveryRepository repository() {
        DeliveryRepository deliveryRepository = DeliveryApplication.applicationContext.getBean(
            DeliveryRepository.class
        );
        return deliveryRepository;
    }

    public static void addToDeliveryList(OrderPlaced orderPlaced) {
        /** Example 1:  new item
        */ 
        Delivery delivery = new Delivery();
        delivery.setCustomerId(orderPlaced.getCustomerId());
        delivery.setOrderId(orderPlaced.getId());
        repository().save(delivery);


        /** Example 2:  finding and process
        
        repository().findById(orderPlaced.get???()).ifPresent(delivery->{
            
            delivery // do something
            repository().save(delivery);

            DeliveryStarted deliveryStarted = new DeliveryStarted(delivery);
            deliveryStarted.publishAfterCommit();

         });
        */

    }
}
