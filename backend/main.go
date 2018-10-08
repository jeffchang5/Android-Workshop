package main

import (
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/gin-gonic/gin"
	"github.com/globalsign/mgo"
	"github.com/globalsign/mgo/bson"
	_ "github.com/heroku/x/hmetrics/onload"
)

var db *mgo.Database

const (
	budgetURL                = "ds125453.mlab.com:25453"
	budgetItemCollectionName = "budget"
)

type BudgetItem struct {
	ID       bson.ObjectId `json:"id,omitempty" bson:"_id,omitempty"`
	Name     string        `json:"name" bson:"name"`
	Category string        `json:"category" bson:"category"`
	Amount   float32       `json:"amount" bson:"amount"`
}

func main() {
	port := os.Getenv("PORT")

	if port == "" {
		log.Fatal("$PORT must be set")
	}

	connect()

	router := gin.New()
	router.Use(gin.Logger())

	router.GET("/", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{
			"success": "Use /budget to interact with the budget model.",
		})
	})

	router.GET("/budget", func(c *gin.Context) {
		budgetList := []BudgetItem{}
		err := db.C(budgetItemCollectionName).Find(nil).All(&budgetList)

		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{
				"error": "Couldn't get list of budget items.",
			})
		}
		c.JSON(http.StatusOK, budgetList)
	})

	router.POST("/budget", func(c *gin.Context) {
		var newBudgetItem BudgetItem

		err := c.Bind(&newBudgetItem)
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{
				"error": "Couldn't parse budget item.",
			})
		}
		newBudgetItem.ID = bson.NewObjectId()
		fmt.Println(newBudgetItem)
		error := db.C(budgetItemCollectionName).Insert(newBudgetItem)
		if error != nil {
			c.JSON(http.StatusBadRequest, gin.H{
				"error": "Couldn't create a new budget item.",
			})
		} else {
			c.JSON(http.StatusOK, newBudgetItem)
		}
	})

	router.Run(":" + port)
}

func connect() {
	session, err := mgo.Dial(budgetURL)

	if err != nil {
		fmt.Println("An error has occured")
	}

	db = session.DB("heroku_fz31f389")
	db.Login("jeffrey", "password1")
}
