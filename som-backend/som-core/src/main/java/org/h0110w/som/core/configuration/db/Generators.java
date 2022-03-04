package org.h0110w.som.core.configuration.db;

@org.hibernate.annotations.GenericGenerator(
        name = Generators.UUID_GENERATOR,
        strategy = "uuid2"
)
@org.hibernate.annotations.GenericGenerator(
        name = Generators.LONG_ID_GENERATOR,
        strategy = "enhanced-sequence"
)
//@org.hibernate.annotations.GenericGenerator(
//        name = Generators.TASK_ID_GENERATOR,
//        strategy = "enhanced-sequence"
//)
public class Generators {
    private Generators(){
    }
    public static final String UUID_GENERATOR = "UUID_GENERATOR";
    public static final String LONG_ID_GENERATOR = "LONG_ID_GENERATOR";
    public static final String TASK_ID_GENERATOR = "TASK_ID_GENERATOR";
}

