/*
Navicat SQLite Data Transfer

Source Server         : meineStadt.de
Source Server Version : 30706
Source Host           : :0

Target Server Type    : SQLite
Target Server Version : 30706
File Encoding         : 65001

Date: 2012-06-19 14:31:44
*/

PRAGMA foreign_keys = OFF;

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
CONSTRAINT "street_id" FOREIGN KEY ("street_id") REFERENCES "streets" ("id")
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
