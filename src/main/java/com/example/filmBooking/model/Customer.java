package com.example.filmBooking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private String id;

    @Column(name = "code")
    private String code;

    @NotEmpty(message = "Họ tên không được để trống")
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "rankCustomer_id")
    private RankCustomer rankCustomer;

    @NotEmpty(message = "SĐT không được để trống")
    @Pattern(regexp="^0\\d{9,10}$", message="SĐT phải đúng định dạng")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email sai định dạng mail")
    @Column(name = "email")
    private String email;


    @NotEmpty(message = "Password không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có độ dài tối thiểu 8 ký tự")
    @Column(name = "password")
    private String password;

    @Column(name = "point")
    private Integer point;
}
