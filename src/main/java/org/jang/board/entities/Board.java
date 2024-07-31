package org.jang.board.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jang.global.entities.BaseMemberEntitiy;
@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Board extends BaseMemberEntitiy {
    @Id
    @Column(length = 30)
    private String bId;
    @Column(length = 60, nullable = false)
    private String bName;
}
