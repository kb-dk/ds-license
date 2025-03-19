INSERT INTO DR_HOLDBACK_RULES (id, name, days) VALUES ('2.01.01', 'Almene Nyheder', 30);
INSERT INTO DR_HOLDBACK_RULES (id, name, days) VALUES ('2.01.02', 'Sportsnyheder', 30);
INSERT INTO DR_HOLDBACK_RULES (id, name, days) VALUES ('2.02', 'Aktualitet og Debat', 2190);
INSERT INTO DR_HOLDBACK_RULES (id, name, days) VALUES ('2.03', 'Oplysning og Kultur', 2190);
INSERT INTO DR_HOLDBACK_RULES (id, name, days) VALUES ('2.04', 'Undervisning', 2190);
INSERT INTO DR_HOLDBACK_RULES (id, name, days) VALUES ('2.05', 'Dramatik & Fiktion', 3650);
INSERT INTO DR_HOLDBACK_RULES (id, name, days) VALUES ('2.06', 'Musik', 2190);
INSERT INTO DR_HOLDBACK_RULES (id, name, days) VALUES ('2.07', 'Underholdning', 2190);
INSERT INTO DR_HOLDBACK_RULES (id, name, days) VALUES ('2.08', 'Sport', 2190);
INSERT INTO DR_HOLDBACK_RULES (id, name, days) VALUES ('2.09', 'Præsentation', 2190);
INSERT INTO DR_HOLDBACK_RULES (id, name, days) VALUES ('2.09*', 'Øvrigt (TTV + Pauser)', 2190);
INSERT INTO DR_HOLDBACK_RULES (id, name, days) VALUES ('2.20', 'Sponsorskilte', 2190);

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (1, 1000, 1900, 1000, 1000, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (2, 1000, 1900, 1100, 1100, '2.01.01');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (3, 1000, 1900, 1200, 1500, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (4, 1000, 1900, 1600, 1600, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (5, 1000, 1900, 1700, 1700, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (6, 1000, 1900, 1800, 1800, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (7, 1000, 1900, 1900, 1900, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (8, 1000, 1900, 2000, 2300, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (9, 1000, 1900, 3000, 3900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (10, 1000, 1900, 4000, 4900, '2.02');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (11, 2000, 2900, 1000, 1000, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (12, 2000, 2900, 1200, 1500, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (13, 2000, 2900, 1600, 1600, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (14, 2000, 2900, 1700, 1700, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (15, 2000, 2900, 1800, 1800, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (16, 2000, 2900, 1900, 1900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (17, 2000, 2900, 2000, 2300, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (18, 2000, 2900, 3000, 3900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (19, 2000, 2900, 4000, 4900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (20, 2000, 2900, 6200, 6900, '2.03');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (21, 3000, 3190, 1000, 1000, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (22, 3000, 3190, 1200, 1500, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (23, 3000, 3190, 1600, 1600, '2.05');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (24, 3000, 3190, 1700, 1700, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (25, 3000, 3190, 1900, 1900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (26, 3000, 3190, 2000, 2300, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (27, 3000, 3190, 3000, 3900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (28, 3000, 3190, 4000, 4900, '2.05');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (29, 3200, 3390, 1000, 1000, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (30, 3200, 3390, 1200, 1500, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (31, 3200, 3390, 1600, 1600, '2.06');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (32, 3200, 3390, 1700, 1700, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (33, 3200, 3390, 1900, 1900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (34, 3200, 3390, 2000, 2300, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (35, 3200, 3390, 3000, 3900, '2.06');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (36, 3200, 3390, 4000, 4900, '2.06');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (37, 3200, 3390, 5000, 5900, '2.06');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (38, 4000, 4900, 1000, 1000, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (39, 4000, 4900, 1200, 1500, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (40, 4000, 4900, 1600, 1600, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (41, 4000, 4900, 1700, 1700, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (42, 4000, 4900, 1900, 1900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (43, 4000, 4900, 2000, 2300, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (44, 4000, 4900, 3000, 3900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (45, 4000, 4900, 4000, 4900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (46, 4000, 4900, 5000, 5900, '2.06');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (47, 5000, 5990, 1600, 1600, '2.08');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (48, 5000, 5990, 3000, 3900, '2.08');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (49, 5000, 5990, 6000, 6000, '2.08');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (50, 5000, 5990, 6100, 6100, '2.01.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (51, 5000, 5990, 6200, 6900, '2.08');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (52, 6000, 6300, 1000, 1000, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (53, 6000, 6300, 1200, 1500, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (54, 6000, 6300, 1600, 1600, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (55, 6000, 6300, 1700, 1700, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (56, 6000, 6300, 1800, 1800, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (57, 6000, 6300, 1900, 1900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (58, 6000, 6300, 2000, 2300, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (59, 6000, 6300, 3000, 3900, '2.07');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (60, 6400, 6400, 1000, 1000, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (61, 6400, 6400, 1200, 1500, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (62, 6400, 6400, 1600, 1600, '2.05');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (63, 6400, 6400, 1700, 1700, '2.05');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (64, 6400, 6400, 1800, 1800, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (65, 6400, 6400, 1900, 1900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (66, 6400, 6400, 2000, 2300, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (67, 6400, 6400, 3000, 3900, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (68, 6400, 6400, 4000, 4900, '2.05');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (69, 6500, 6600, 1000, 1000, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (70, 6500, 6600, 1200, 1500, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (71, 6500, 6600, 1600, 1600, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (72, 6500, 6600, 1700, 1700, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (73, 6500, 6600, 1800, 1800, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (74, 6500, 6600, 1900, 1900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (75, 6500, 6600, 2000, 2300, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (76, 6500, 6600, 3000, 3900, '2.07');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (77, 6700, 6700, 1000, 1000, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (78, 6700, 6700, 1200, 1500, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (79, 6700, 6700, 1600, 1600, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (80, 6700, 6700, 1700, 1700, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (81, 6700, 6700, 1800, 1800, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (82, 6700, 6700, 1900, 1900, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (83, 6700, 6700, 2000, 2300, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (84, 6700, 6700, 3000, 3900, '2.07');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (85, 6800, 6900, 1000, 1000, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (86, 6800, 6900, 1200, 1500, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (87, 6800, 6900, 1600, 1600, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (88, 6800, 6900, 1700, 1700, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (89, 6800, 6900, 1800, 1800, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (90, 6800, 6900, 1900, 1900, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (91, 6800, 6900, 2000, 2300, '2.07');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (92, 6800, 6900, 3000, 3900, '2.07');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (93, 7100, 7100, 7000, 7000, '2.09');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (94, 7120, 7120, 1000, 1000, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (95, 7120, 7120, 1100, 1100, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (96, 7120, 7120, 1200, 1500, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (97, 7120, 7120, 1600, 1600, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (98, 7120, 7120, 1700, 1700, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (99, 7120, 7120, 1800, 1800, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (100, 7120, 7120, 1900, 1900, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (101, 7120, 7120, 2000, 2300, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (102, 7120, 7120, 3000, 3900, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (103, 7120, 7120, 4000, 4900, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (104, 7120, 7120, 5000, 5900, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (105, 7120, 7120, 6000, 6000, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (106, 7120, 7120, 6100, 6100, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (107, 7120, 7120, 6200, 6900, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (108, 7120, 7120, 7000, 7000, '2.02');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (109, 7130, 7130, 1000, 1000, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (110, 7130, 7130, 1100, 1100, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (111, 7130, 7130, 1200, 1500, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (112, 7130, 7130, 1600, 1600, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (113, 7130, 7130, 1700, 1700, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (114, 7130, 7130, 1800, 1800, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (115, 7130, 7130, 1900, 1900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (116, 7130, 7130, 2000, 2300, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (117, 7130, 7130, 3000, 3900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (118, 7130, 7130, 4000, 4900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (119, 7130, 7130, 5000, 5900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (120, 7130, 7130, 6000, 6000, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (121, 7130, 7130, 6100, 6100, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (122, 7130, 7130, 6200, 6900, '2.03');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (123, 7130, 7130, 7000, 7000, '2.03');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (124, 7140, 7150, 1000, 1000, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (125, 7140, 7150, 1100, 1100, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (126, 7140, 7150, 1200, 1500, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (127, 7140, 7150, 1600, 1600, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (128, 7140, 7150, 1700, 1700, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (129, 7140, 7150, 1800, 1800, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (130, 7140, 7150, 1900, 1900, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (131, 7140, 7150, 2000, 2300, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (132, 7140, 7150, 3000, 3900, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (133, 7140, 7150, 4000, 4900, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (134, 7140, 7150, 5000, 5900, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (135, 7140, 7150, 6000, 6000, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (136, 7140, 7150, 6100, 6100, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (137, 7140, 7150, 6200, 6900, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (138, 7140, 7150, 7000, 7000, '2.09*');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (139, 7160, 7212, 1200, 1500, '2.09');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (140, 7160, 7212, 1900, 1900, '2.09');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (141, 7160, 7212, 7000, 7000, '2.09');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (142, 7220, 7220, 1000, 1000, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (143, 7220, 7220, 1100, 1100, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (144, 7220, 7220, 1200, 1500, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (145, 7220, 7220, 1600, 1600, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (146, 7220, 7220, 1700, 1700, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (147, 7220, 7220, 1800, 1800, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (148, 7220, 7220, 1900, 1900, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (149, 7220, 7220, 2000, 2300, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (150, 7220, 7220, 3000, 3900, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (151, 7220, 7220, 4000, 4900, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (152, 7220, 7220, 5000, 5900, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (153, 7220, 7220, 6000, 6000, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (154, 7220, 7220, 6100, 6100, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (155, 7220, 7220, 6200, 6900, '2.02');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (156, 7220, 7220, 7000, 7000, '2.02');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (157, 7230, 7252, 7000, 7000, '2.09');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (158, 7260, 7260, 1000, 1000, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (159, 7260, 7260, 1100, 1100, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (160, 7260, 7260, 1200, 1500, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (161, 7260, 7260, 1600, 1600, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (162, 7260, 7260, 1700, 1700, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (163, 7260, 7260, 1800, 1800, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (164, 7260, 7260, 1900, 1900, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (165, 7260, 7260, 2000, 2300, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (166, 7260, 7260, 3000, 3900, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (167, 7260, 7260, 4000, 4900, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (168, 7260, 7260, 5000, 5900, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (169, 7260, 7260, 6000, 6000, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (170, 7260, 7260, 6100, 6100, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (171, 7260, 7260, 6200, 6900, '2.09*');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (172, 7260, 7260, 7000, 7000, '2.09*');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (173, 7270, 7270, 1000, 1000, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (174, 7270, 7270, 1100, 1100, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (175, 7270, 7270, 1200, 1500, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (176, 7270, 7270, 1600, 1600, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (177, 7270, 7270, 1700, 1700, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (178, 7270, 7270, 1800, 1800, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (179, 7270, 7270, 1900, 1900, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (180, 7270, 7270, 2000, 2300, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (181, 7270, 7270, 3000, 3900, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (182, 7270, 7270, 4000, 4900, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (183, 7270, 7270, 5000, 5900, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (184, 7270, 7270, 6000, 6000, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (185, 7270, 7270, 6100, 6100, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (186, 7270, 7270, 6200, 6900, '2.20');
INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (187, 7270, 7270, 7000, 7000, '2.20');

INSERT INTO DR_HOLDBACK_MAP(id, content_range_from, content_range_to, form_range_from, form_range_to, dr_holdback_id) VALUES (188, 7280, 7290, 7000, 7000, '2.09');
