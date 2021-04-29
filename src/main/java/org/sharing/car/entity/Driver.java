/*
 *    Copyright 2021 Bhaskar Gogoi
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.sharing.car.entity;


import lombok.Data;
import org.sharing.car.domainvalue.OnlineStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "drivers")
public class Driver implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "age", nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OnlineStatus status;

    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private ZonedDateTime updatedDate;

    private Driver() {
    }

    public Driver(String firstName, String lastName, String email, String password, int age, OnlineStatus status, ZonedDateTime createdDate, ZonedDateTime updatedDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.age = age;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
