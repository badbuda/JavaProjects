#include <stdio.h> /*printf*/
#include <stdlib.h> /*free*/
#include "enum_rd58.h"

#define SIZE 5

typedef struct class class_t;
typedef struct object object_t;
typedef void (*v_func) (void*);

char returned_string[256] = {0};
int counter = 0;

enum object_enum{TOSTRING, FINALIZE, SAYHELLO};

struct class
{
	char *name;
	size_t size;
	class_t *super;
	v_func *(vtable[]);
};

struct object
{
	class_t* metadata;
};

typedef struct animal
{
	object_t obj;
	int num_legs;
	int num_masters;
	int ID;
}animal_t;

typedef struct dog
{
	animal_t animal;
	int num_legs;
}dog_t;

typedef struct cat
{
	animal_t animal;
	char *colors;
	int num_masters;
}cat_t;

typedef struct legendaryAnimal
{
	cat_t cat;
}legendaryAnimal_t;

/********************************* declarition ********************************/
void ObjectFinalize(object_t *this);
int ObjectHashCode(object_t *this);
char *ObjectToString(object_t *this);
object_t *ObjectAlloc(class_t *class);

void AnimalSayHello(animal_t *this);
void AnimalShowCounter();
int AnimalGetNumMasters(animal_t *this);
void AnimalFinalize(animal_t *this);
char *AnimalToString(animal_t *this);
void AnimalStatic1();
void AnimalStatic2();
void AnimalInstance();

void DogSayHello(dog_t *this);
void DogFinalize(dog_t *this);
char *DogToString(dog_t *this);
void DogStatic();
void DogInstance();

void CatFinalize(cat_t *this);
char *CatToString(cat_t *this);
void CatStatic();

void legendaryAnimalSayHello();
void LegendaryAnimalFinalize(legendaryAnimal_t *this);
char *LegendaryAnimalToString(legendaryAnimal_t *this);
void LegendaryAnimalStatic();
/******************************** v_func **************************************/

v_func object_v_table[] = {(v_func)ObjectToString,
			(v_func)ObjectFinalize };

v_func animal_v_table[] = {
			(v_func)AnimalToString,
			(v_func)AnimalFinalize,
			(v_func)AnimalSayHello };

v_func dog_v_table[] = {
			(v_func)DogToString,
			(v_func)DogFinalize,
			(v_func)DogSayHello };

v_func cat_v_table[] = {
			(v_func)CatToString,
			(v_func)CatFinalize,
			(v_func)AnimalSayHello };

v_func legendaryAnimal_v_table[] = {
			(v_func)LegendaryAnimalToString,
			(v_func)LegendaryAnimalFinalize,
			(v_func)legendaryAnimalSayHello };

/*********************** functions defenition - Metadata ************************/

class_t object_metadata = {"object", sizeof(object_t), NULL, object_v_table};
class_t animal_metadata = {"java2c.animal", sizeof(animal_t), &object_metadata, animal_v_table};
class_t cat_metadata = {"animal.Cat", sizeof(cat_t), &animal_metadata, cat_v_table};
class_t dog_metadata = {"animal.Dog", sizeof(dog_t), &animal_metadata, dog_v_table};
class_t legendaryAnimal_metadata = {"animal.LegendaryAnimal",
      sizeof(legendaryAnimal_v_table), &cat_metadata, legendaryAnimal_v_table};

/*********************** functions defenition - Object ************************/
void ObjectFinalize(object_t *this)
{
	free(this);
}

int ObjectHashCode(object_t *this)
{
	long size = 31 * (long)this / 10000;
	return((int)size);
}

char *ObjectToString(object_t *this)
{
	sprintf(returned_string, "%s@%d\n", this->metadata->name, ObjectHashCode(this));
	return (returned_string);
}

object_t *ObjectAlloc(class_t *class)
{
	object_t *alloc_object = calloc(class->size, sizeof(char));
	alloc_object->metadata = class;

	return (alloc_object);
}

/*********************** functions defenition - animal ************************/
void AnimalSayHello(animal_t *this)
{
	printf("Animal Hello!\n");
	printf("I have %d legs\n", this->num_legs);
}

void AnimalShowCounter()
{
	printf("%d\n", counter);
}

int AnimalGetNumMasters(animal_t *this)
{
	return (this->num_masters);
}

void AnimalFinalize(animal_t *this)
{
	printf("finalize Animal with ID: %d\n", this->ID);
	ObjectFinalize((object_t*)this);
}

char *AnimalToString(animal_t *this)
{
	sprintf(returned_string, "Animal with ID: %d\n", this->ID);
	return (returned_string);
}

void AnimalStatic1()
{
	static int has_been_called = FALSE;

	if(!has_been_called)
	{
		printf("Static block Animal 1\n");
		has_been_called = TRUE;
	}
}

void AnimalInstance()
{
	printf("Instance initialization block Animal\n");
}

void AnimalStatic2()
{
	static int has_been_called2 = FALSE;

	if(!has_been_called2)
	{
		printf("Static block Animal 2\n");
		has_been_called2 = TRUE;
	}
}

/*********************** functions defenition - Dog ***************************/
void DogSayHello(dog_t *this)
{
	printf("Dog Hello!\n");
	printf("I have %d legs\n", this->num_legs);
}

void DogFinalize(dog_t *this)
{
	printf("finalize Dog with ID: %d\n", this->animal.ID);
	AnimalFinalize((animal_t*)this);
}

char *DogToString(dog_t *this)
{
	sprintf(returned_string, "Dog with ID: %d\n",this->animal.ID);
	return (returned_string);
}

void DogStatic()
{
	static int has_been_called = FALSE;

	if(!has_been_called)
	{
		printf("Static block Dog\n");
			has_been_called = TRUE;
	}
}

void DogInstance()
{
	printf("instance initialization block Dog\n");
}

/*********************** functions defenition - cat ***************************/
void CatFinalize(cat_t *this)
{
	printf("finalize Cat with ID: %d\n", this->animal.ID);
	DogFinalize((dog_t*)this);
}

char *CatToString(cat_t *this)
{
	sprintf(returned_string, "Cat with ID: %d\n",this->animal.ID);
	return (returned_string);
}

void CatStatic()
{
	static int has_been_called = FALSE;

	if(!has_been_called)
	{
		printf("Static block Cat\n");
		has_been_called = TRUE;
	}
}

/*********************** functions defenition - legendary Animal **************/
void legendaryAnimalSayHello()
{
	printf("Legendary Hello! \n");
}

void LegendaryAnimalFinalize(legendaryAnimal_t *this)
{
	printf("finalize legendaryAnimal with ID: %d\n", this->cat.animal.ID);
	CatToString((cat_t*)this);
}

char *LegendaryAnimalToString(legendaryAnimal_t *this)
{
	sprintf(returned_string, "LegendaryAnimal with ID: %d\n",this->cat.animal.ID);
	return (returned_string);
}

void LegendaryAnimalStatic()
{
	static int has_been_called = FALSE;

	if(!has_been_called)
	{
	  	printf("Static block Legendary Animal\n");
			has_been_called = TRUE;
	}
}
/****************************** constructor ***********************************/
void AnimalCTOR1(animal_t *this)
{
	AnimalStatic1();
	AnimalStatic2();
	AnimalInstance();
	printf("Animal Ctor\n");
	this->ID = ++counter;
	this->num_legs = 5;
	this->num_masters = 1;

	((void (*)(void*))((*this->obj.metadata->vtable)[SAYHELLO]))(this);
	AnimalShowCounter();

	printf("%s", ((char *(*)(void*))((*this->obj.metadata->vtable)[TOSTRING]))(this));

	printf("%s",ObjectToString((object_t*)this));
}

void AnimalCTOR2(animal_t *this, int num_masters)
{
	AnimalStatic1();
	AnimalStatic2();
	AnimalInstance();

	printf("Animal Ctor int \n");
	this->ID = ++counter;
	this->num_masters = num_masters;
}

void DogCTOR(dog_t *this)
{
	this->num_legs = 4;
	AnimalStatic1();
	AnimalStatic2();

	DogStatic();
	AnimalCTOR2(&this->animal, 2);
	DogInstance();
	printf("Dog Ctor \n");
}

void CatCTOR2(cat_t *this, char *colors)
{
	AnimalStatic1();
	AnimalStatic2();
	CatStatic();
	AnimalCTOR1(&this->animal);

	this->colors = colors;
	printf("Cat Ctor with color: %s \n", this->colors);
}

void CatCTOR1(cat_t *this)
{
	CatCTOR2(this,"black");
	printf("Cat Ctor \n");
	this->num_masters = 2;
}

void legendaryAnima_CTOR(legendaryAnimal_t *this)
{
	AnimalStatic1();
	AnimalStatic2();
	CatStatic();
	LegendaryAnimalStatic();
	CatCTOR1(&this->cat);

	printf("Legendary Ctor \n");
}

void foo(animal_t *this)
{
	printf("%s", ((char *(*)(void*))((*this->obj.metadata->vtable)[TOSTRING]))(this));
}

/************************************ main ************************************/
int main (void)
{
	int i = 0;
	object_t *object = ObjectAlloc(&object_metadata);
	animal_t *animal_obj = (animal_t*)ObjectAlloc(&animal_metadata);
	AnimalCTOR1((animal_t *)animal_obj);
	dog_t *dog_obj = (dog_t*)ObjectAlloc(&dog_metadata);
	DogCTOR((dog_t *)dog_obj);
	cat_t *cat_obj = (cat_t*)ObjectAlloc(&cat_metadata);
	CatCTOR1((cat_t *)cat_obj);
	legendaryAnimal_t *legendary_animal_obj = (legendaryAnimal_t*)ObjectAlloc(&legendaryAnimal_metadata);
	legendaryAnima_CTOR((legendaryAnimal_t*)legendary_animal_obj);

	AnimalShowCounter();

	printf("%d\n", animal_obj->ID);
	printf("%d\n", dog_obj->animal.ID);
	printf("%d\n", cat_obj->animal.ID);
	printf("%d\n", legendary_animal_obj->cat.animal.ID);

	dog_t *dog_obj2 = (dog_t*)ObjectAlloc(&dog_metadata);
	DogCTOR((dog_t *)dog_obj2);
	cat_t *cat_obj2 = (cat_t*)ObjectAlloc(&cat_metadata);
	CatCTOR1((cat_t *)cat_obj2);
	cat_t *cat_obj3 = (cat_t*)ObjectAlloc(&cat_metadata);
	CatCTOR2((cat_t *)cat_obj3, "white");
	legendaryAnimal_t *legendary_animal_obj2 =
		(legendaryAnimal_t*)ObjectAlloc(&legendaryAnimal_metadata);
	legendaryAnima_CTOR((legendaryAnimal_t*)legendary_animal_obj2);
	animal_t *animal_obj2 = (animal_t*)ObjectAlloc(&animal_metadata);
	AnimalCTOR1((animal_t *)animal_obj2);

	animal_t* array[] = {
		((animal_t *)dog_obj2),
		((animal_t *)cat_obj2),
		((animal_t *)cat_obj3),
		((animal_t *)legendary_animal_obj2),
		((animal_t *)animal_obj2)
	};


	for(i = 0; i < SIZE; ++i)
	{
		((*array[i]->obj.metadata->vtable)[SAYHELLO])(array[i]);
		printf("%d\n", array[i]->num_masters);
	}

	for(i = 0; i < SIZE; ++i)
	{
		foo(array[i]);
		ObjectFinalize((object_t *)array[i]);
	}

	ObjectFinalize((object_t *)object);
	ObjectFinalize((object_t *)animal_obj);
	ObjectFinalize((object_t *)dog_obj);
	ObjectFinalize((object_t *)cat_obj);
	ObjectFinalize((object_t *)legendary_animal_obj);

	return (0);
}
