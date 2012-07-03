/*
Navicat SQLite Data Transfer

Source Server         : meineStadt.de
Source Server Version : 30706
Source Host           : :0

Target Server Type    : SQLite
Target Server Version : 30706
File Encoding         : 65001

Date: 2012-06-20 17:59:18
*/

PRAGMA foreign_keys = OFF;

-- ----------------------------
-- Table structure for "main"."branches"
-- ----------------------------
DROP TABLE "main"."branches";
CREATE TABLE "branches" (
"id"  INTEGER NOT NULL,
"name"  TEXT,
"href"  TEXT,
PRIMARY KEY ("id" ASC)
);

-- ----------------------------
-- Records of branches
-- ----------------------------

-- ----------------------------
-- Table structure for "main"."specialBuildings"
-- ----------------------------
DROP TABLE "main"."specialBuildings";
CREATE TABLE "specialBuildings" (
"id"  INTEGER,
"name"  VARCHAR,
"street_id"  INTEGER,
"hnr"  VARCHAR,
"branch_id"  INTEGER,
PRIMARY KEY ("id" ASC),
CONSTRAINT "fkey0" FOREIGN KEY ("street_id") REFERENCES "streets" ("id"),
FOREIGN KEY ("branch_id") REFERENCES "branches" ("id")
);

-- ----------------------------
-- Records of specialBuildings
-- ----------------------------

-- ----------------------------
-- Table structure for "main"."streets"
-- ----------------------------
DROP TABLE "main"."streets";
CREATE TABLE "streets" (
"id"  INTEGER,
"name"  TEXT,
PRIMARY KEY ("id")
);

-- ----------------------------
-- Records of streets
-- ----------------------------
