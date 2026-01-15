package com.project.byeoryback.domain.payment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaopayReadyResponse {
    private String tid;
    private String next_redirect_pc_url;
    private String next_redirect_mobile_url;
    private String created_at;
}
