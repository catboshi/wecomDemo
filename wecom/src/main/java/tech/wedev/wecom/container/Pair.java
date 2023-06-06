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
public class Pair<L, R> implements Serializable {
    private L left;
    private R right;

    public static <L, R> Pair<L, R> of(L left, R right) {
        return Pair.<L, R>builder().left(left).right(right).build();
    }

    public static <L, R> List<Pair<L, R>> of(Pair<L, R>... pairs) {
        return ArrayUtils.asArrayList(pairs);
    }
}
