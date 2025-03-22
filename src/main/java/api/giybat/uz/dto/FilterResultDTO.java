package api.giybat.uz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class FilterResultDTO <T>{
    private Long totalCount;
    private List<T> content;
}
