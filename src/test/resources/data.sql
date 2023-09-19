INSERT
    INTO
        UNICORNS(
            ID,
            NAME,
            MANE_COLOR,
            HORN_LENGTH,
            HORN_DIAMETER,
            DATE_OF_BIRTH
        )
    VALUES(
        uuid(),
        'Grace',
        'RAINBOW',
        42,
        10,
        DATE '1982-2-19'
    );
