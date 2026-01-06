package com.project.byeoryback.domain.market.repository;

import com.project.byeoryback.domain.market.entity.MarketItem;
import com.project.byeoryback.domain.market.entity.MarketItemStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class MarketItemSpecification {

    public static Specification<MarketItem> hasStatus(MarketItemStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<MarketItem> hasSellerId(Long sellerId) {
        return (root, query, criteriaBuilder) -> {
            if (sellerId != null && sellerId.equals(0L)) {
                return criteriaBuilder.isNull(root.get("seller"));
            }
            if (sellerId == null) {
                return null; // Or handle as "no filtering"
            }
            return criteriaBuilder.equal(root.get("seller").get("id"), sellerId);
        };
    }

    public static Specification<MarketItem> containsKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return null;
            }
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("name"), likePattern),
                    criteriaBuilder.like(root.get("contentJson"), likePattern));
        };
    }

    public static Specification<MarketItem> containsTags(List<String> tags) {
        return (root, query, criteriaBuilder) -> {
            if (tags == null || tags.isEmpty()) {
                return null;
            }
            Specification<MarketItem> spec = Specification.where(null);
            for (String tag : tags) {
                String likePattern = "%" + tag + "%";
                spec = spec.and((r, q, cb) -> cb.like(r.get("contentJson"), likePattern));
            }
            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

    public static Specification<MarketItem> isFree() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("price"), 0L);
    }
}
