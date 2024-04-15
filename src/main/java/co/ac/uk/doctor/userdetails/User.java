package co.ac.uk.doctor.userdetails;

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
