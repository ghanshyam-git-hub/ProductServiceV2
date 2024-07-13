CREATE TABLE category
(
    uuid       BINARY(16)   NOT NULL,
    is_deleted BIT(1) NOT NULL,
    category   VARCHAR(255) NULL,
    CONSTRAINT pk_category PRIMARY KEY (uuid)
);

CREATE TABLE product
(
    uuid          BINARY(16)   NOT NULL,
    is_deleted    BIT(1) NOT NULL,
    product_id    BIGINT NULL,
    title         VARCHAR(255) NULL,
    price DOUBLE NULL,
    category_uuid BINARY(16)   NULL,
    `description` VARCHAR(255) NULL,
    imageurl      VARCHAR(255) NULL,
    CONSTRAINT pk_product PRIMARY KEY (uuid)
);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_CATEGORY_UUID FOREIGN KEY (category_uuid) REFERENCES category (uuid);