package canard.intern.post.following.backend.dto;

import canard.intern.post.following.backend.enums.PoeType;

public interface IPoeTypeCountPoeDto {
    PoeType getPoeType();
    long getCountPoe();
}
