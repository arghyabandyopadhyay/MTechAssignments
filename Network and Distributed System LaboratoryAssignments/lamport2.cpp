#include <iostream>
#include<stdio.h>
class q

{

	int ts,si;

	

};
class lamport

{
	int main()
	{

		int ts1,ts2,ts3,req1=0,req2=0,req3=0;

		printf("Enter Time Stamp for Site S1 :");

		cin >> ts1;

		printf("Enter Time Stamp for Site S2 :");

		cin >>ts2;

		printf("Enter Time Stamp for Site S3 :");

		cin >>ts3;

		 

		printf("Is Site S1 Send a CS request : Y/N =");

		cin >> res;

		if(res.equalsIgnoreCase("y"))

		req1=1;

		 

		printf("Is Site S2 Send a CS request : Y/N =");

		cin >>res;

		if(res.equalsIgnoreCase("y"))

		req2=1;

		 

		printf("Is Site S3 Send a CS request : Y/N =");

		cin >>res;

		if(res.equalsIgnoreCase("y"))

		req3=1;

 

		q obj2=new q();

		obj2.ts=ts2;

		obj2.si=2;

		 

		q obj1=new q();

		obj1.ts=ts1;

		obj1.si=1;

		 

		q obj3=new q();

		obj3.ts=ts3;

		obj3.si=3;

 

 

		q obj4=new q();

		obj4.ts=ts3;

		obj4.si=100;

		 

		if(req2==1)

		{

			lamport l1=new lamport();

			 

			l1.stack(obj2,obj1,1);

			lamport l2=new lamport();

			 

			l2.stack(obj2,obj3,2);

		}

		lamport l3=new lamport();

		if(req1==1)

		{

			l3.stack(obj1,obj2,1);
			lamport l4=new lamport();
			l4.stack(obj1,obj3,2);

		}

 

		if(req3==1)

		{

			lamport l5=new lamport();
			l5.stack(obj3,obj1,1);
			lamport l6=new lamport();
			l6.stack(obj3,obj2,2);

		}

		if(req1==1)

		{

			printf("REQUEST(S1) :[ ("+obj1.a.ts+","+obj1.a.si+")("+obj1.b.ts+","+obj1.b.si+")]");

			printf("REQUEST(S1) :[ ("+obj1.c.ts+","+obj1.c.si+")("+obj1.d.ts+","+obj1.d.si+")]");

		}

		if(req2==1)

		{

			printf("REQUEST(S2) :[ ("+obj2.a.ts+","+obj2.a.si+")("+obj2.b.ts+","+obj2.b.si+")]");

			printf("REQUEST(S2) :[ ("+obj2.c.ts+","+obj2.c.si+")("+obj2.d.ts+","+obj2.d.si+")]");

		}

		if(req3==1)

		{

			printf("REQUEST(S3) :[ ("+obj3.a.ts+","+obj3.a.si+")("+obj3.b.ts+","+obj3.b.si+")]");

			printf("REQUEST(S3) :[ ("+obj3.c.ts+","+obj3.c.si+")("+obj3.d.ts+","+obj3.d.si+")]");

		}

		 

		if((obj2.a.ts==obj2.c.ts)&&(obj2.a.si==obj2.c.si)&&(obj2.a.si==2))

		{

			printf(" SIte S2 Executing in CS ");

			l3.rel(obj2,obj2,1);l3.rel(obj2,obj2,2);

			l3.rel(obj2,obj1,1);

			printf(" After Releasing CS ");

			 

			printf("REQUEST(S2) :[ ("+obj2.a.ts+","+obj2.a.si+")]");

			printf("REQUEST(S2) :[ ("+obj2.c.ts+","+obj2.c.si+")]");

			printf("REQUEST(S1) :[ ("+obj1.a.ts+","+obj1.a.si+")("+obj1.b.ts+","+obj1.b.si+")]");

			printf("REQUEST(S1) :[ ("+obj1.c.ts+","+obj1.c.si+")("+obj1.d.ts+","+obj1.d.si+")]");

			printf(" SIte S1 Executing in CS ");

		}

}

void stack(q objcal,q objcmp,int r)

{

if((objcal.ts)<(objcmp.ts)) {
	if(r==1)
	{
		objcal.a=objcal;

		objcal.b=objcmp;

	} else {

		objcal.c=objcal;

		objcal.d=objcmp;
	}
} else {
	if(r==1)

	{

		objcal.b=objcal;

		objcal.a=objcmp;

	} else {

		objcal.d=objcal;

		objcal.c=objcmp;
	}

}

}

 

public void rel(q objcl,q objrm,int rm)

{

 

if(objcl.a.si==objrm.a.si)

{

if(rm==1)

{

objrm.a=objrm.b;

objrm.b=null;

}

else

{

objrm.c=objrm.d;

objrm.b=null;

}

}

 

}
}
