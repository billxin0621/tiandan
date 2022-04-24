package com.sinosoft.fragins.framework.utils;



import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelProperty
{
  String value() default "";

  String name() default "";

  String allowableValues() default "";

  String access() default "";

  String notes() default "";

  String dataType() default "";

  boolean required() default false;

  int position() default 0;

  boolean hidden() default false;

  String example() default "";

  @Deprecated
  boolean readOnly() default false;

  AccessMode accessMode() default AccessMode.AUTO;

  String reference() default "";

  boolean allowEmptyValue() default false;

  Extension[] extensions() default {@Extension(properties={@ExtensionProperty(name="", value="")})};

  public static enum AccessMode
  {
    AUTO,  READ_ONLY,  READ_WRITE;

    private AccessMode() {}
  }
}
