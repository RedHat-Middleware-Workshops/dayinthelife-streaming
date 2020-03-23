#psql -d sampledb -U user

CREATE TABLE premium (
	mmid bigint NOT NULL,
	diameter integer NOT NULL,
    weight decimal NOT NULL,
	created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);


CREATE TABLE standard (
	weight decimal NOT NULL,
	created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);
INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);
INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);
INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);
INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);
INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);
INSERT INTO premium(mmid,diameter, weight) VALUES (4567845678456, 4, 2.3);