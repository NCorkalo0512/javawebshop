package com.example.hralgebrajavawebshop.models;


import com.example.hralgebrajavawebshop.dto.OrderDTO;
import com.example.hralgebrajavawebshop.dto.OrderItemDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOrder;
    @Column(name = "OrderTime")
    private Date orderTime;
    @Column(name = "TotalAmount")
    private BigDecimal totalAmount;
    @Column(name = "PaymentMethod")
    private String paymentMethod;

    @Column(name = "PaypalTransactionId", nullable = true)
    private String paypalTransactionId;

    @Column(name = "PaymentStatus", nullable = true)
    private String paymentStatus;
    @ManyToOne
    @JoinColumn(name = "UsersID", referencedColumnName = "idUsers")
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    public void setOrderItems(List<OrderItem> items) {
        this.items = items;
    }
    public OrderDTO OrderToDTO() {
        OrderDTO dto = new OrderDTO();
        dto.setUserId(this.user != null ? this.user.getIdUsers() : null);
        dto.setTotalAmount(this.totalAmount);
        dto.setPaymentMethod(this.paymentMethod);

        List<OrderItemDTO> itemDTOs = this.items.stream()
                .map(OrderItem::OrderItemtoDTO)
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);

        return dto;
    }
}
