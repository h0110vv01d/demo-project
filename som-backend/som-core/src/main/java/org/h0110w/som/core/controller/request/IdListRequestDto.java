package org.h0110w.som.core.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * dto used for cases when you need to transfer list of longs
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdListRequestDto {
    private List<String> ids;
}
