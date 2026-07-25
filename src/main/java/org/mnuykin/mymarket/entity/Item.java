package org.mnuykin.mymarket.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Item {
    @Id
    @Column("id")
    private Long id;
    @Column("title")
    private String title;
    @Column("description")
    private String description;
    @Column("img_path")
    private String imgPath;
    @Column("price")
    private Long price;

    @Transient
    private Integer count = 0;
}
