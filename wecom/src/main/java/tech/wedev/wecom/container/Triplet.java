package tech.wedev.wecom.container;

import tech.wedev.wecom.utils.ArrayUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Triplet<L, C, R> implements Serializable {
    private L left;
    private C center;
    private R right;

    public static <L, C, R> Triplet<L, C, R> of(L left, C center, R right) {
        return Triplet.<L, C, R>builder().left(left).center(center).right(right).build();
    }

    public static <L, C, R> List<Triplet<L, C, R>> of(Triplet<L, C, R>... triplets) {
        return ArrayUtils.asArrayList(triplets);
    }
}
