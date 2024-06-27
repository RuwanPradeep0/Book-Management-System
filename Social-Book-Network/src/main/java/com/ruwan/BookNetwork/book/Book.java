package com.ruwan.BookNetwork.book;

import com.ruwan.BookNetwork.common.BaseEntity;
import com.ruwan.BookNetwork.feedback.Feedback;
import com.ruwan.BookNetwork.history.BookTransactionHistory;
import com.ruwan.BookNetwork.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {


    private String title;

    private String authorName;

    private String synopsis;

    private String bookCover;

    private String isbn;

    private boolean archived;

    private boolean shareable;



    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

    @Transient
    public  double getRate(){
        if(feedbacks == null || feedbacks.isEmpty()){
            return  0.0;
        }

        var rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);

        double roundedRate = Math.round(rate * 10.0)/10.0;

        return roundedRate;

    }




}
