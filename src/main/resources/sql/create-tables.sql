CREATE TABLE user (
                      userId BIGINT PRIMARY KEY,
                      firstName VARCHAR(255) NOT NULL,
                      lastName VARCHAR(255) NOT NULL,
                      userName VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      isActive BOOLEAN NOT NULL
);


CREATE TABLE trainingType (
                              typeId BIGINT PRIMARY KEY,
                              typeName VARCHAR(255) NOT NULL
);


CREATE TABLE trainee (
                         traineeId BIGINT PRIMARY KEY,
                         dateOfBirth DATE,
                         address VARCHAR(255),
                         userId BIGINT NOT NULL,
                         FOREIGN KEY (userId) REFERENCES user(userId)
);

CREATE TABLE trainer (
                         trainerId BIGINT PRIMARY KEY,
                         specialization BIGINT NOT NULL,
                         userId BIGINT NOT NULL,
                         FOREIGN KEY (specialization) REFERENCES trainingType(typeId),
                         FOREIGN KEY (userId) REFERENCES user(userId)
);

CREATE TABLE training (
                          trainingId BIGINT PRIMARY KEY,
                          trainingName VARCHAR(255) NOT NULL,
                          trainingDate DATE NOT NULL,
                          trainingDuration NUMERIC NOT NULL,
                          traineeId BIGINT NOT NULL,
                          trainerId BIGINT NOT NULL,
                          typeId BIGINT NOT NULL,
                          FOREIGN KEY (traineeId) REFERENCES trainee(traineeId),
                          FOREIGN KEY (trainerId) REFERENCES trainer(trainerId),
                          FOREIGN KEY (typeId) REFERENCES trainingType(typeId)
);

