package com.seekhog.backend.dto;

import com.seekhog.backend.model.Friendship;
import lombok.Data;

@Data
public class FriendshipResponse {
    private String id; // Changed Long to String
    private String requesterId;
    private String requesterName;
    private String addresseeId;
    private String addresseeName;
    private String status;
    private Long totalLogs;

    public FriendshipResponse(Friendship f, String reqName, String addrName, Long totalLogs) {
        this.id = f.getId();
        this.requesterId = f.getRequesterId();
        this.requesterName = reqName;
        this.addresseeId = f.getAddresseeId();
        this.addresseeName = addrName;
        this.status = f.getStatus().toString();
        this.totalLogs = totalLogs;
    }
}