#psql -d sampledb -U dbuser

CREATE TABLE premium (
	mmid bigint NOT NULL,
	diameter integer NOT NULL,
    weight decimal NOT NULL
);


CREATE TABLE standard (
	weight decimal NOT NULL
);

INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);
INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);
INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);
INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);
INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);
INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);
INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);