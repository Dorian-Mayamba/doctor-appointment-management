package co.ac.uk.doctor.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class User{

    private Status status;

    static enum Status{
        ONLINE,OFFLINE,BUSY
    }
}
