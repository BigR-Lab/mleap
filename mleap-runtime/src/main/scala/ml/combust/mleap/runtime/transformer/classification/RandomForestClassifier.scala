package ml.combust.mleap.runtime.transformer.classification

import ml.combust.mleap.classification.RandomForestClassifierModel
import ml.combust.mleap.runtime.attribute.{AttributeSchema, CategoricalAttribute}
import ml.combust.mleap.runtime.transformer.Transformer
import ml.combust.mleap.runtime.transformer.builder.TransformBuilder
import ml.combust.mleap.runtime.transformer.builder.TransformBuilder.Ops
import ml.combust.mleap.runtime.types.{DoubleType, TensorType}

import scala.util.Try

/**
  * Created by hollinwilkins on 3/30/16.
  */
case class RandomForestClassifier(uid: String = Transformer.uniqueName("random_forest_classification"),
                                  featuresCol: String,
                                  predictionCol: String,
                                  model: RandomForestClassifierModel) extends Transformer {
  override def build[TB: TransformBuilder](builder: TB): Try[TB] = {
    builder.withInput(featuresCol, TensorType.doubleVector()).flatMap {
      case (b, featuresIndex) =>
        b.withOutput(predictionCol, DoubleType)(row => model(row.getVector(featuresIndex)))
    }
  }

  override def transformAttributeSchema(schema: AttributeSchema): AttributeSchema = {
    schema.withField(predictionCol, CategoricalAttribute())
  }
}