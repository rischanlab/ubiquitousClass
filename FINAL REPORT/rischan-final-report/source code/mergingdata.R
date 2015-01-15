#Coded by Rischan Mafrur
file_list <- list.files("D:/Dropbox/LAB/COURSE/3/ubi/projects/features/alvin/resultA", full.names = TRUE)

merged_data <- data.frame()
for(files in file_list)
{
  temp <- read.csv(sprintf(files),header=TRUE)
  merged_data <- rbind(merged_data,temp)
}
merged_data <- merged_data
activity <- "alvin_walkingA"
write.csv(merged_data,sprintf("D:/Dropbox/LAB/COURSE/3/ubi/projects/features/alvin/resultA/%s_TF.csv",activity))
