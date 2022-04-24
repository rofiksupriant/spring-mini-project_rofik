package com.rofik.miniproject.domain.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

interface ApiError {

}

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
class ApiValidationError implements ApiError {
   private String object;
   private String field;
   private Object rejectedValue;
   private String message;

   ApiValidationError(String object, String message) {
       this.object = object;
       this.message = message;
   }
}