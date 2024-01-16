CREATE
    TABLE
        IF NOT EXISTS unicorns(
            id uuid NOT NULL,
            name VARCHAR NOT NULL,
            mane_color VARCHAR,
            horn_length INT,
            horn_diameter INT,
            date_of_birth DATE,
            PRIMARY KEY(id)
        );
