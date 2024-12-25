CREATE TABLE chat_messages
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    sender_id  BIGINT                                  NOT NULL,
    group_id   BIGINT                                  NOT NULL,
    content    TEXT                                    NOT NULL,
    type       VARCHAR(255)                            NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_chat_messages PRIMARY KEY (id)
);

CREATE TABLE comments
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    post_id    BIGINT                                  NOT NULL,
    user_id    BIGINT                                  NOT NULL,
    content    TEXT                                    NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);

CREATE TABLE group_chat_members
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    group_chat_id BIGINT                                  NOT NULL,
    user_id       BIGINT                                  NOT NULL,
    joined_at     TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_group_chat_members PRIMARY KEY (id)
);

CREATE TABLE group_chats
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name       VARCHAR(255)                            NOT NULL,
    admin_id   BIGINT                                  NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_group_chats PRIMARY KEY (id)
);

CREATE TABLE image
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    path          VARCHAR(255)                            NOT NULL,
    is_compressed BOOLEAN                                 NOT NULL,
    uploaded_at   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_image PRIMARY KEY (id)
);

CREATE TABLE likes
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    post_id  BIGINT                                   NOT NULL,
    user_id  BIGINT                                   NOT NULL,
    liked_at TIMESTAMP WITHOUT TIME ZONE              NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (id)
);

CREATE TABLE locations
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    country   VARCHAR(255),
    city      VARCHAR(255),
    address   VARCHAR(255),
    number    INTEGER                                 NOT NULL,
    latitude  DOUBLE PRECISION                        NOT NULL,
    longitude DOUBLE PRECISION                        NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE messages
(
    id           INTEGER NOT NULL,
    content      TEXT    NOT NULL,
    sent_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    sender_id    BIGINT,
    recipient_id BIGINT,
    is_read      BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TABLE notifications
(
    id      INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id BIGINT,
    content TEXT                                     NOT NULL,
    sent_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);

CREATE TABLE pet_care_locations
(
    id          INTEGER          NOT NULL,
    name        VARCHAR(255)     NOT NULL,
    latitude    DOUBLE PRECISION NOT NULL,
    longitude   DOUBLE PRECISION NOT NULL,
    description TEXT,
    CONSTRAINT pk_pet_care_locations PRIMARY KEY (id)
);

CREATE TABLE posts
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description TEXT                                    NOT NULL,
    image_id    BIGINT,
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    user_id     BIGINT,
    location_id BIGINT,
    CONSTRAINT pk_posts PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE user_followers
(
    follower_id BIGINT NOT NULL,
    user_id     BIGINT NOT NULL,
    CONSTRAINT pk_user_followers PRIMARY KEY (follower_id, user_id)
);

CREATE TABLE users
(
    id                       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username                 VARCHAR(100)                            NOT NULL,
    password                 VARCHAR(255)                            NOT NULL,
    email                    VARCHAR(255)                            NOT NULL,
    first_name               VARCHAR(100)                            NOT NULL,
    last_name                VARCHAR(100)                            NOT NULL,
    address                  VARCHAR(255),
    enabled                  BOOLEAN,
    last_password_reset_date TIMESTAMP WITHOUT TIME ZONE,
    last_login_date          TIMESTAMP WITHOUT TIME ZONE,
    activation_token         VARCHAR(255),
    role_id                  BIGINT,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE posts
    ADD CONSTRAINT uc_posts_image UNIQUE (image_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE chat_messages
    ADD CONSTRAINT FK_CHAT_MESSAGES_ON_GROUP FOREIGN KEY (group_id) REFERENCES group_chats (id);

ALTER TABLE chat_messages
    ADD CONSTRAINT FK_CHAT_MESSAGES_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (id);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE group_chats
    ADD CONSTRAINT FK_GROUP_CHATS_ON_ADMIN FOREIGN KEY (admin_id) REFERENCES users (id);

ALTER TABLE group_chat_members
    ADD CONSTRAINT FK_GROUP_CHAT_MEMBERS_ON_GROUP_CHAT FOREIGN KEY (group_chat_id) REFERENCES group_chats (id);

ALTER TABLE group_chat_members
    ADD CONSTRAINT FK_GROUP_CHAT_MEMBERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

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
    ADD CONSTRAINT FK_POSTS_ON_IMAGE FOREIGN KEY (image_id) REFERENCES image (id);

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_LOCATION FOREIGN KEY (location_id) REFERENCES locations (id);

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_ROLE FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_followers
    ADD CONSTRAINT fk_usefol_on_follower FOREIGN KEY (follower_id) REFERENCES users (id);

ALTER TABLE user_followers
    ADD CONSTRAINT fk_usefol_on_user FOREIGN KEY (user_id) REFERENCES users (id);