-- 漫画貸出管理システム
-- データベースおよびテーブル初期設定

CREATE DATABASE IF NOT EXISTS `library_db_jp`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE `library_db_jp`;

SET NAMES utf8mb4;


-- ==================================================
-- 会員テーブル
-- ==================================================

CREATE TABLE IF NOT EXISTS `EX_MEMBER` (
    `MEMBER_ID` INT NOT NULL AUTO_INCREMENT,
    `NAME` VARCHAR(50) NOT NULL,
    `PHONE` VARCHAR(20) NOT NULL,
    `EMAIL` VARCHAR(100) NOT NULL,
    `PASSWORD` VARCHAR(100) NOT NULL,

    PRIMARY KEY (`MEMBER_ID`),
    UNIQUE KEY `UK_MEMBER_PHONE` (`PHONE`),
    UNIQUE KEY `UK_MEMBER_EMAIL` (`EMAIL`)
) ENGINE=InnoDB
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;


-- ==================================================
-- 書籍テーブル
-- ==================================================

CREATE TABLE IF NOT EXISTS `EX_BOOK` (
    `BOOK_ID` INT NOT NULL AUTO_INCREMENT,
    `TITLE` VARCHAR(100) NOT NULL,
    `AUTHOR` VARCHAR(100) NOT NULL,
    `PUBLISHER` VARCHAR(100) NOT NULL,
    `BOOK_STATUS` VARCHAR(20) NOT NULL,

    PRIMARY KEY (`BOOK_ID`)
) ENGINE=InnoDB
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;


-- ==================================================
-- 貸出テーブル
-- ==================================================

CREATE TABLE IF NOT EXISTS `EX_RENTAL` (
    `RENTAL_ID` INT NOT NULL AUTO_INCREMENT,
    `MEMBER_ID` INT NOT NULL,
    `BOOK_ID` INT NOT NULL,
    `RENTAL_DATE` DATE NOT NULL,
    `DUE_DATE` DATE NOT NULL,
    `RETURN_DATE` DATE DEFAULT NULL,
    `RENTAL_STATUS` VARCHAR(20) NOT NULL,

    PRIMARY KEY (`RENTAL_ID`),

    KEY `IDX_RENTAL_MEMBER_ID` (`MEMBER_ID`),
    KEY `IDX_RENTAL_BOOK_ID` (`BOOK_ID`),

    CONSTRAINT `FK_RENTAL_MEMBER`
        FOREIGN KEY (`MEMBER_ID`)
        REFERENCES `EX_MEMBER` (`MEMBER_ID`),

    CONSTRAINT `FK_RENTAL_BOOK`
        FOREIGN KEY (`BOOK_ID`)
        REFERENCES `EX_BOOK` (`BOOK_ID`)
) ENGINE=InnoDB
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;