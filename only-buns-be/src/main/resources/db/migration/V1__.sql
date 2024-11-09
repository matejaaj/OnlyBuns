CREATE TABLE comments
(
    id         INTEGER DEFAULT nextval('comments_id_seq') NOT NULL,
    post_id    INTEGER,
    user_id    INTEGER,
    content    TEXT                                       NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()                      NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);

CREATE TABLE likes
(
    id       INTEGER DEFAULT nextval('likes_id_seq') NOT NULL,
    post_id  INTEGER,
    user_id  INTEGER,
    liked_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()                   NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (id)
);

CREATE TABLE messages
(
    id           INTEGER DEFAULT nextval('messages_id_seq') NOT NULL,
    content      TEXT                                       NOT NULL,
    sent_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()                      NOT NULL,
    sender_id    INTEGER,
    recipient_id INTEGER,
    is_read      BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TABLE notifications
(
    id      INTEGER DEFAULT nextval('notifications_id_seq') NOT NULL,
    user_id INTEGER,
    content TEXT                                            NOT NULL,
    sent_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()                           NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);

CREATE TABLE pet_care_locations
(
    id          INTEGER DEFAULT nextval('pet_care_locations_id_seq') NOT NULL,
    name        VARCHAR(255)                                         NOT NULL,
    latitude    DOUBLE PRECISION                                     NOT NULL,
    longitude   DOUBLE PRECISION                                     NOT NULL,
    description TEXT,
    CONSTRAINT pk_pet_care_locations PRIMARY KEY (id)
);

CREATE TABLE posts
(
    id          INTEGER DEFAULT nextval('posts_id_seq') NOT NULL,
    description TEXT                                    NOT NULL,
    image_url   VARCHAR(255),
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()                   NOT NULL,
    latitude    DOUBLE PRECISION,
    longitude   DOUBLE PRECISION,
    user_id     INTEGER,
    CONSTRAINT pk_posts PRIMARY KEY (id)
);

CREATE TABLE user_followers
(
    user_id     INTEGER NOT NULL,
    follower_id INTEGER NOT NULL,
    CONSTRAINT pk_user_followers PRIMARY KEY (user_id, follower_id)
);

CREATE TABLE users
(
    id            INTEGER DEFAULT nextval('users_id_seq') NOT NULL,
    email         VARCHAR(255)                            NOT NULL,
    username      VARCHAR(100)                            NOT NULL,
    password      VARCHAR(255)                            NOT NULL,
    first_name    VARCHAR(100)                            NOT NULL,
    last_name     VARCHAR(100)                            NOT NULL,
    address       VARCHAR(255),
    is_active     BOOLEAN DEFAULT FALSE,
    registered_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()                   NOT NULL,
    role          VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;

ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_RECIPIENT FOREIGN KEY (recipient_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE user_followers
    ADD CONSTRAINT FK_USER_FOLLOWERS_ON_FOLLOWER FOREIGN KEY (follower_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE user_followers
    ADD CONSTRAINT FK_USER_FOLLOWERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;