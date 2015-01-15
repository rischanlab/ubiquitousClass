#Coded by Rischan Mafrur
#install.packages("e1071")
library("e1071")

setwd("D:/Dropbox/LAB/COURSE/3/ubi/projects/data_training/csv")
#SVM model classifier
feature_data <- read.csv("tf_tot.csv",header = TRUE)
head(feature_data)
x <- subset(feature_data, select=-label)
y <- feature_data$label

#creating training model
svm_model <- svm(x,y,kernel="radial",cost=1, gamma =0.5)
summary(svm_model)

pred <- predict(svm_model,x)
system.time(pred <- predict(svm_model,x))
#Testing of Model Performances
table(pred,y)

#If the result is good, apply that model to testing data set

#Model Result

# y
# pred      agung alvin gde rischan
# agung     249     1   3       6
# alvin       1   226   7       1
# gde         2    11 257      29
# rischan     5     3   7     168

setwd("D:/Dropbox/LAB/COURSE/3/ubi/projects/data_testing/csv")
test_data <- read.csv("tf_tot.csv", header = TRUE)
x1 <- subset(test_data, select=-label)
y1 <- test_data$label

test_pred <- predict(svm_model,x1)
table(test_pred,y1)

#Final Result

# y1
# test_pred agung alvin gde rischan
# agung     202    14   6      16
# alvin       0   195  16      15
# gde        58    30 244      55
# rischan     8    12  17     147



#accuracy <- data.frame(pred,y)

