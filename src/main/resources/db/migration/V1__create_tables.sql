-- ========================
-- ITEM
-- ========================
CREATE TABLE item (
    id          BINARY(16)        NOT NULL,
    name        VARCHAR(100)    NOT NULL,
    description TEXT,
    type        VARCHAR(50)     NOT NULL,
    bonus       INT             NOT NULL,
    stat        VARCHAR(10)     NOT NULL,

    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT uq_item_name UNIQUE (name)
);

-- ========================
-- HERO
-- ========================
CREATE TABLE hero (
    id      BINARY(16)        NOT NULL,
    name    VARCHAR(100)    NOT NULL,
    pv      INT             NOT NULL    DEFAULT 10,
    atq     INT             NOT NULL    DEFAULT 2,
    def     INT             NOT NULL    DEFAULT 0,
    speed   INT             NOT NULL    DEFAULT 1,
    exp     INT             NOT NULL    DEFAULT 0,
    race    VARCHAR(20)     NOT NULL,
    role    VARCHAR(20)     NOT NULL,

    CONSTRAINT pk_hero PRIMARY KEY (id),
    CONSTRAINT uq_hero_name UNIQUE (name)
);

-- ========================
-- HERO_ITEM
-- ========================
CREATE TABLE hero_item (
    hero_id    		BINARY(16)    NOT NULL,
    item_id         BINARY(16)    NOT NULL,
    slot            VARCHAR(20) NOT NULL,
    equipped        BOOLEAN     NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_hero_item PRIMARY KEY (hero_id, item_id, slot),
    CONSTRAINT fk_hero_item_hero FOREIGN KEY (hero_id) REFERENCES hero (id) ON DELETE CASCADE,
    CONSTRAINT fk_hero_item_item FOREIGN KEY (item_id) REFERENCES item (id) ON DELETE CASCADE
);