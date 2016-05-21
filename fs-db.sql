--Term Project database
--Created by
--Tianxin Chu, tic30
--Yincheng He, yih39
--	***Please 	1. Run fs-db.sql to create database.
--	 			2. Run createdata.java to insert data.
--  			3. Run FaceSpace.java to test individual function.
----------------------------------------------
DROP TABLE profile CASCADE CONSTRAINTS;
DROP TABLE friends CASCADE CONSTRAINTS;
DROP TABLE groups CASCADE CONSTRAINTS;
DROP TABLE groupMem CASCADE CONSTRAINTS;
DROP TABLE messages CASCADE CONSTRAINTS;

CREATE TABLE profile
(   user_ID 	number(10) PRIMARY KEY,
    username   	varchar2(32),
	email		varchar2(32),
    dob    		TIMESTAMP,
	lastLogin   TIMESTAMP
	--CONSTRAINT profile_unique_name_and_dob UNIQUE (fname,lname,dob)
);

--in oracle, need a sequence to do the AUTO_INCREMENT
--CREATE SEQUENCE seq_person MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 10;
--INSERT INTO Persons (ID,FirstName,LastName) VALUES (seq_person.nextval,'Lars','Monsen') --seq_person.nextval
--mySQL: AUTO_INCREMENT=1

CREATE TABLE friends
(   sender_ID 	number(10),
	receiver_ID number(10),
	fstatus 	number(1),
	CONSTRAINT friends_PK PRIMARY KEY (sender_ID, receiver_ID),
	CONSTRAINT friendsender_FK_profile FOREIGN KEY (sender_ID) REFERENCES profile(user_ID) ON DELETE CASCADE,
	CONSTRAINT friendreceiver_FK_profile FOREIGN KEY (receiver_ID) REFERENCES profile(user_ID) ON DELETE CASCADE
);

CREATE TABLE groups
(	groupID 	number(10) PRIMARY KEY,
	gname		varchar2(100),
	description	varchar2(100),
	memlimit 	number(10)--trigger done, see below
);

CREATE TABLE groupMem
(	groupID 	number(10),
	user_ID 	number(10),
	CONSTRAINT groupMem_PK PRIMARY KEY (groupID, user_ID),
	CONSTRAINT groupuser_FK_profile FOREIGN KEY (user_ID) REFERENCES profile(user_ID) ON DELETE CASCADE,
	CONSTRAINT groupID_FK_group FOREIGN KEY (groupID) REFERENCES groups(groupID) ON DELETE CASCADE
);

CREATE TABLE messages
(	msgID 		number(10) PRIMARY KEY,
	sender_ID 	number(10),
	receiver_ID number(10),
	subject 	varchar2(100),
	bodytext	varchar2(100),
	date_sent	TIMESTAMP,
	CONSTRAINT 	msgsender_FK_profile FOREIGN KEY (sender_ID) REFERENCES profile(user_ID) ON DELETE SET NULL,
	CONSTRAINT 	msgreceiver_FK_profile FOREIGN KEY (receiver_ID) REFERENCES profile(user_ID) ON DELETE SET NULL
);


--mem limit trigger --done
create OR replace trigger memLimitTrigger before insert or update on groupMem
FOR EACH ROW
declare 
	mycount INTEGER;
	mylimit INTEGER;
begin
	SELECT count(*) into mycount from groupMem where groupMem.groupID=:new.groupID;
    SELECT memlimit into mylimit from groups where groups.groupID=:new.groupID; 
	if (mycount >= mylimit) then
		RAISE_APPLICATION_ERROR(-20101, 'Exceeds max member limit');
	end if;
end;
/

--msg delete trigger
create OR replace trigger msgDeleteTrigger before insert or update on messages
FOR EACH ROW
begin
	if ((:new.sender_ID is null) and (:new.receiver_ID is null)) then
		--delete from messages where msgID=:new.msgID;
		:new.msgID := -1;
	end if;
end;
/